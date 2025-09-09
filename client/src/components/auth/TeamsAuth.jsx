import React, { useEffect, useState, useCallback } from "react";
import * as microsoftTeams from "@microsoft/teams-js";
import { useMsal } from "@azure/msal-react";
import { useDispatch } from "react-redux";
import { setUser, setToken } from "@/features/auth/authSlice";
import { loginRequest } from "@/config/msalConfig";

function TeamsAuth({ children }) {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);
  const [contextType, setContextType] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const { instance } = useMsal();
  const dispatch = useDispatch();

  const handleTeamsAuth = useCallback(() => {
    return new Promise((resolve, reject) => {
      microsoftTeams.authentication.getAuthToken({
        successCallback: async (token) => {
          try {
            console.log("Teams token acquired");
            const payload = JSON.parse(atob(token.split(".")[1]));
            const userInfo = {
              email: payload.upn || payload.preferred_username,
              name: payload.name,
              userId: payload.oid || payload.sub,
            };
            setUserInfo(userInfo);
            
            // Update Redux store
            dispatch(setToken(token));
            dispatch(setUser(userInfo));
            
            resolve(userInfo);
          } catch (error) {
            console.error("Error processing Teams token:", error);
            reject(error);
          }
        },
        failureCallback: (reason) => {
          console.error("Teams auth failed:", reason);
          setError(`Auth failed in Teams: ${reason}`);
          reject(new Error(reason));
        },
      });
    });
  }, [dispatch]);

  const handleBrowserAuth = useCallback(async () => {
    try {
      const accounts = instance.getAllAccounts();
      console.log("Accounts found:", accounts.length);

      if (accounts.length > 0) {
        // Try silent token acquisition
        try {
          const tokenResponse = await instance.acquireTokenSilent({
            ...loginRequest,
            account: accounts[0],
          });
          console.log("Silent auth successful");
          const userInfo = {
            email: tokenResponse.account.username,
            name: tokenResponse.account.name,
            userId: tokenResponse.account.localAccountId,
          };
          setUserInfo(userInfo);
          
          // Update Redux store
          dispatch(setToken(tokenResponse.accessToken));
          dispatch(setUser(userInfo));
          
          return userInfo;
        } catch (silentError) {
          console.log("Silent auth failed, trying popup:", silentError);
          // Fall through to popup login
        }
      }

      // Popup login
      console.log("Triggering popup login");
      const loginResponse = await instance.loginPopup(loginRequest);
      console.log("Login popup successful");
      
      const tokenResponse = await instance.acquireTokenSilent({
        ...loginRequest,
        account: loginResponse.account,
      });
      
      const userInfo = {
        email: tokenResponse.account.username,
        name: tokenResponse.account.name,
        userId: tokenResponse.account.localAccountId,
      };
      setUserInfo(userInfo);
      
      // Update Redux store
      dispatch(setToken(tokenResponse.accessToken));
      dispatch(setUser(userInfo));
      
      return userInfo;
    } catch (error) {
      console.error("Browser auth failed:", error);
      setError(`Browser auth failed: ${error.message}`);
      throw error;
    }
  }, [instance, dispatch]);

  const detectTeamsContext = useCallback(() => {
    return new Promise((resolve) => {
      if (typeof microsoftTeams !== "object") {
        resolve(false);
        return;
      }

      try {
        // Try to get Teams context
        microsoftTeams.app.initialize().then(() => {
          microsoftTeams.app.getContext().then((context) => {
            console.log("Teams context detected:", context);
            const clientType = context.app.host.clientType;
            setContextType(clientType === "desktop" ? "teams-desktop" : "teams-web");
            resolve(true);
          }).catch((error) => {
            console.log("Failed to get Teams context:", error);
            resolve(false);
          });
        }).catch((error) => {
          console.log("Teams initialization failed:", error);
          resolve(false);
        });
      } catch (error) {
        console.log("Teams detection error:", error);
        resolve(false);
      }
    });
  }, []);

  useEffect(() => {
    console.log("useEffect started - detecting context");
    setIsLoading(true);

    const initializeAuth = async () => {
      try {
        const isTeams = await detectTeamsContext();
        
        if (isTeams) {
          console.log("Initializing Teams authentication");
          await handleTeamsAuth();
        } else {
          console.log("Initializing browser authentication");
          setContextType("browser");
          await handleBrowserAuth();
        }
      } catch (error) {
        console.error("Authentication initialization failed:", error);
        setContextType("browser");
        setError(`Authentication failed: ${error.message}`);
        
        // Fallback to browser auth if Teams auth fails
        if (contextType !== "browser") {
          try {
            await handleBrowserAuth();
          } catch (browserError) {
            console.error("Fallback browser auth also failed:", browserError);
          }
        }
      } finally {
        setIsLoading(false);
      }
    };

    initializeAuth();
  }, [detectTeamsContext, handleTeamsAuth, handleBrowserAuth, contextType]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 via-cyan-100 to-blue-200">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500 mx-auto mb-4"></div>
          <p className="text-lg font-mono">Authenticating...</p>
          <p className="text-sm text-gray-600 mt-2">Context: {contextType}</p>
        </div>
      </div>
    );
  }

  if (error && !userInfo) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-red-100 to-red-200">
        <div className="bg-white/90 shadow-2xl rounded-2xl px-8 py-12 w-full max-w-md text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-4">Authentication Error</h2>
          <p className="text-red-500 mb-4">{error}</p>
          <p className="text-sm text-gray-600 mb-4">Context: {contextType}</p>
          <button 
            onClick={() => window.location.reload()} 
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  if (!userInfo) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-yellow-100 to-yellow-200">
        <div className="bg-white/90 shadow-2xl rounded-2xl px-8 py-12 w-full max-w-md text-center">
          <h2 className="text-2xl font-bold text-yellow-600 mb-4">Authentication Required</h2>
          <p className="text-yellow-500 mb-4">Please sign in to continue</p>
          <button 
            onClick={handleBrowserAuth} 
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
          >
            Sign In with Microsoft
          </button>
        </div>
      </div>
    );
  }

  // User is authenticated, render children with user info display
  return (
    <div>
      <div className="bg-green-50 border-l-4 border-green-400 p-4 mb-4">
        <div className="flex">
          <div className="ml-3">
            <p className="text-sm text-green-700">
              <strong>Authenticated:</strong> {userInfo.name} ({userInfo.email}) 
              <span className="ml-2 text-green-600">[{contextType}]</span>
            </p>
          </div>
        </div>
      </div>
      {children}
    </div>
  );
}

export default TeamsAuth;