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
import Task from "@/pages/Task/Task.jsx";
import FinalTest from "@/pages/FinalTest/FinalTest.jsx";
import Topics from "@/pages/Topics/Topics.jsx";
import RequireAdmin from "@/hoc/RequireAdmin.jsx";
import CreateTask from "@/pages/Admin/Tasks/CreateTask.jsx";
import Topic from "@/pages/Topics/Topics.jsx";
import TasksList from "@/pages/TasksList/TasksList.jsx";
import RatingPage from "@/pages/Rating/RatingPage.jsx";
import MaterialsListPage from "@/pages/MaterialList/MaterialListPage.jsx";
import MaterialDetailPage from "@/pages/MaterialDetail/MaterialDetailPage.jsx";
import AdminDashboard from "@/pages/Admin/AdminDashboard.jsx";
import RequireTeacher from "@/hoc/RequireTeacher.jsx";
import TeacherDashboard from "@/pages/Teacher/TeacherDashboard.jsx";

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
          <ProtectedRoute allowedRoles={["PLAYER"]}>
            <Profile />
          </ProtectedRoute>
        ),
      },
      {
        path: "/play",
        element: (
          <ProtectedRoute allowedRoles={["PLAYER"]}>
            <DifficultyLevel />
          </ProtectedRoute>
        ),
      },
      {
        path: "/level/:difficulty",
        element: (
            <ProtectedRoute allowedRoles={["PLAYER"]}>
              <Topic />
            </ProtectedRoute>
        ),
      },
      {
        path: "/level/:difficulty/topic/:topicName/task/:taskId",
        element: (
            <ProtectedRoute allowedRoles={["PLAYER"]}>
              <Task/>
            </ProtectedRoute>
        )
      },
      {
        path: "/level/:difficulty/final-test",
        element: (
            <ProtectedRoute allowedRoles={["PLAYER"]}>
              <FinalTest/>
            </ProtectedRoute>
        )
      },
      {path: "/training",
      element: <Topics/>
      },
      {
        path: "/level/:difficulty/topic/:topicName", // New route for TasksList
        element: (
            <ProtectedRoute allowedRoles={["PLAYER"]}>
              <TasksList />
            </ProtectedRoute>
        ),
      },
      {
        path: "/admin/tasks/create",
        element: <RequireAdmin>
          <CreateTask />
        </RequireAdmin>
      },
      {
        path: "/leaderboard",
        element: (<ProtectedRoute allowedRoles={["PLAYER", "ADMIN", "TEACHER"]}>
          <RatingPage/>
        </ProtectedRoute>)
      },
      {
        path: "/materials",
        element: (
            <ProtectedRoute allowedRoles={["PLAYER", "ADMIN", "TEACHER"]}>
              <MaterialsListPage/>
            </ProtectedRoute>
        )
      },
      {
        path: "/materials/:id",
        element: (
            <ProtectedRoute allowedRoles={["PLAYER", "ADMIN", "TEACHER"]}>
              <MaterialDetailPage />
            </ProtectedRoute>
        )
      },
      {
        path: "/admin",
        element: (
            <RequireAdmin>
              <AdminDashboard/>
            </RequireAdmin>
        )
      },
      {
        path: "/teacher",
        element: (
            <RequireTeacher>
              <TeacherDashboard />
            </RequireTeacher>
        )
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
