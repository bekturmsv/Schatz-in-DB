import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "react-redux";
import "./index.css";
import "./styles/theme.css";
import "./utils/i18n";
import { store } from "./store/store";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import Home from "./pages/Home/Home";
import Login from "@/pages/Login/Login.jsx";
import SignUp from "@/pages/Register/Register.jsx";
import Profile from "./pages/Profile/Profile";
import ProtectedRoute from "./components/custom/ProtectedRoute";
import DifficultyLevel from "./pages/DifficultyLevel/DifficultyLevel";
import Level from "./pages/Level/Level";
import Task from "@/pages/Task/Task.jsx";
import FinalTest from "@/pages/FinalTest/FinalTest.jsx";
import Topics from "@/pages/Topics/Topics.jsx";
import TopicDetail from "@/pages/TopicDetails/TopicDetails.jsx";
import RequireAdmin from "@/hoc/RequireAdmin.jsx";
import CreateTask from "@/pages/Admin/Tasks/CreateTask.jsx";

const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "/login",
        element: <Login />,
      },
      {
        path: "/register",
        element: <SignUp />,
      },
      {
        path: "/profile",
        element: (
          <ProtectedRoute>
            <Profile />
          </ProtectedRoute>
        ),
      },
      {
        path: "/play",
        element: (
          <ProtectedRoute>
            <DifficultyLevel />
          </ProtectedRoute>
        ),
      },
      {
        path: "/level/:difficulty",
        element: (
          <ProtectedRoute>
            <Level />
          </ProtectedRoute>
        ),
      },
      {
        path: "/level/:difficulty/task/:taskId",
        element: (
            <ProtectedRoute>
              <Task/>
            </ProtectedRoute>
        )
      },
      {
        path: "level/:difficulty/final-test",
        element: (
            <ProtectedRoute>
              <FinalTest/>
            </ProtectedRoute>
        )
      },
      {path: "/training",
      element: <Topics/>
      },
      {
        path: "/training/:topicId",
        element: <TopicDetail />
      },
      {
        path: "admin/tasks/create",
        element: <RequireAdmin>
          <CreateTask />
        </RequireAdmin>
      }

    ],
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </StrictMode>
);
