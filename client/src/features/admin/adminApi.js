import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const adminApi = createApi({
    reducerPath: "adminApi",
    baseQuery: fetchBaseQuery({
        baseUrl: import.meta.env.VITE_API_BASE_URL,
        prepareHeaders: (headers, { getState }) => {
            const token = getState().auth.token;
            if (token) {
                headers.set("Authorization", `Bearer ${token}`);
            }
            return headers;
        },
    }),
    tagTypes: ['Users', 'Teachers', 'Players'],
    endpoints: (builder) => ({
        getAllUsers: builder.query({
            query: () => "/api/admin/getAllUsers",
            providesTags: ['Users'],
        }),
        getAllTeachers: builder.query({
            query: () => "/api/teacher/getAll",
            providesTags: ['Teachers'],
        }),
        createTeacher: builder.mutation({
            query: (body) => ({
                url: "/api/admin/createTeacher",
                method: "POST",
                body,
            }),
            invalidatesTags: ['Users', 'Teachers'],
        }),
        updateTeacher: builder.mutation({
            query: ({ id, ...body }) => ({
                url: `/api/admin/update/teacher/${id}`,
                method: "PUT",
                body,
            }),
            invalidatesTags: ['Users', 'Teachers'],
        }),
        updatePlayer: builder.mutation({
            query: ({ id, ...body }) => ({
                url: `/api/admin/update/player/${id}`,
                method: "PUT",
                body,
            }),
            invalidatesTags: ['Users', 'Players'],
        }),
    }),
});

export const {
    useGetAllUsersQuery,
    useGetAllTeachersQuery,
    useCreateTeacherMutation,
    useUpdateTeacherMutation,
    useUpdatePlayerMutation,
} = adminApi;
