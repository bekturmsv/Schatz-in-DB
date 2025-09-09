# Microsoft Teams and Browser Authentication Implementation

This document describes the Microsoft Teams and browser authentication implementation for the Schatz-in-DB application.

## Overview

The application now supports authentication in both Microsoft Teams and regular browsers using Microsoft authentication:

- **Teams Context**: Uses Teams SDK authentication tokens
- **Browser Context**: Uses MSAL (Microsoft Authentication Library) with popup authentication
- **Automatic Detection**: Automatically detects whether running in Teams or browser
- **Fallback Support**: Falls back to browser authentication if Teams authentication fails

## Key Features

✅ **Dual Context Support**: Works seamlessly in both Teams and browser environments
✅ **Automatic Context Detection**: No manual switching required
✅ **Error Handling**: Comprehensive error handling with user-friendly messages
✅ **Redux Integration**: Integrates with existing Redux authentication state management
✅ **Loading States**: Shows loading indicators during authentication
✅ **Silent Authentication**: Attempts silent token refresh before requiring user interaction

## Components Added

### 1. MSAL Configuration (`src/config/msalConfig.js`)
- Configures Azure AD authentication
- Sets up scopes and endpoints
- Uses environment variables for security

### 2. TeamsAuth Component (`src/components/auth/TeamsAuth.jsx`)
- Main authentication wrapper component
- Handles Teams and browser authentication flows
- Shows loading, error, and authentication states
- Integrates with Redux store

### 3. Updated Main Application (`src/main.jsx`)
- Wraps app with MsalProvider
- Integrates TeamsAuth wrapper in routing
- Sets up authentication before rendering main content

## Environment Variables

Create a `.env` file based on `.env.example` with the following variables:

```env
VITE_AZURE_CLIENT_ID=your-azure-client-id-here
VITE_AZURE_AUTHORITY=https://login.microsoftonline.com/common
```

## Azure App Registration Setup

To use this authentication system, you need to set up an Azure App Registration:

1. **Register your application** in Azure Portal
2. **Configure Platform Settings**:
   - Add Web platform with redirect URI: `http://localhost:5173` (for development)
   - Add your production URL as redirect URI
3. **API Permissions**:
   - Microsoft Graph: `User.Read` (delegated)
   - Microsoft Graph: `openid` (delegated)
   - Microsoft Graph: `profile` (delegated)
   - Microsoft Graph: `email` (delegated)
4. **Authentication Settings**:
   - Enable "Access tokens" and "ID tokens"
   - Configure for single-page applications (SPA)

## Dependencies Added

```json
{
  "@microsoft/teams-js": "^2.x.x",
  "@azure/msal-react": "^2.x.x", 
  "@azure/msal-browser": "^3.x.x"
}
```

## Authentication Flow

### Browser Authentication
1. Check for existing accounts
2. Try silent token acquisition
3. If no accounts or silent fails, show popup login
4. Store user info and token in Redux

### Teams Authentication  
1. Initialize Teams SDK
2. Get authentication token from Teams
3. Parse JWT token for user information
4. Store user info and token in Redux

### Context Detection
1. Check if Teams SDK is available
2. Try to initialize Teams app
3. If successful, use Teams authentication
4. If failed, fall back to browser authentication

## Error Handling

The component provides comprehensive error handling:

- **Authentication Failures**: Shows retry button with error message
- **Network Issues**: Displays user-friendly error messages
- **Context Detection Failures**: Automatically falls back to browser auth
- **Token Refresh Failures**: Prompts for re-authentication

## Integration with Existing Code

The implementation integrates seamlessly with the existing Redux authentication system:

- Uses existing `authSlice` for state management
- Maintains compatibility with `setUser` and `setToken` actions
- Works with existing `ProtectedRoute` components
- Preserves current role-based access control

## Usage

The authentication is now automatically handled when users visit the application:

1. **In Teams**: Users are automatically authenticated using their Teams context
2. **In Browser**: Users see a Microsoft sign-in popup
3. **After Authentication**: Users can access the application normally

## Testing

To test the implementation:

1. **Browser Testing**: 
   - Start dev server: `npm run dev`
   - Visit `http://localhost:5173`
   - Should see Microsoft authentication popup

2. **Teams Testing**:
   - Deploy to a public URL
   - Add as Teams app with proper manifest
   - Should authenticate automatically in Teams

## Troubleshooting

### Common Issues

1. **"Client ID not configured"**: Set `VITE_AZURE_CLIENT_ID` in `.env`
2. **Popup blocked**: Enable popups for the domain in browser settings
3. **Redirect URI mismatch**: Ensure Azure app registration includes correct URLs
4. **Teams not detected**: Check Teams manifest configuration

### Debug Information

The component logs detailed information to console:
- Context detection results
- Authentication flow steps
- Error messages and stack traces
- Token acquisition status

## Security Considerations

- **Environment Variables**: Never commit actual client IDs to version control
- **HTTPS Required**: Use HTTPS in production for security
- **Token Storage**: Tokens are stored in localStorage (configurable)
- **Scope Limitation**: Only requests necessary Microsoft Graph permissions