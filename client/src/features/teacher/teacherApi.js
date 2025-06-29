import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const teacherApi = createApi({
    reducerPath: "teacherApi",
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
    tagTypes: ['Materials', 'Groups'],
    endpoints: (builder) => ({
        getMaterials: builder.query({
            query: () => "/api/materials",
            providesTags: ['Materials'],
        }),
        getMaterialById: builder.query({
            query: (id) => `/api/materials/${id}`,
        }),
        uploadMaterial: builder.mutation({
            query: ({ title, description, teacherId, sqlKategorie }) => ({
                url: "/api/materials/upload",
                method: "POST",
                params: { title, description, teacherId, sqlKategorie },
            }),
            invalidatesTags: ['Materials'],
        }),
        getGroups: builder.query({
            query: () => "/api/groups/getAll",
            providesTags: ['Groups'],
        }),
        getGroupById: builder.query({
            query: (id) => `/api/groups/getById/${id}`,
        }),
        createGroup: builder.mutation({
            query: (body) => ({
                url: "/api/groups/createGroup",
                method: "POST",
                body,
            }),
            invalidatesTags: ['Groups'],
        }),
        joinGroup: builder.mutation({
            query: (body) => ({
                url: "/api/groups/group",
                method: "POST",
                body,
            }),
            invalidatesTags: ['Groups'],
        }),
    }),
});

export const {
    useGetMaterialsQuery,
    useGetMaterialByIdQuery,
    useUploadMaterialMutation,
    useGetGroupsQuery,
    useGetGroupByIdQuery,
    useCreateGroupMutation,
    useJoinGroupMutation,
} = teacherApi;
