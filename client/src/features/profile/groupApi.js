import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const groupApi = createApi({
    reducerPath: "groupApi",
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
    endpoints: (builder) => ({
        joinGroup: builder.mutation({
            query: ({ groupCode }) => ({
                url: "/api/groups/joinGroup",
                method: "POST",
                body: { groupCode },
            }),
        }),
        quitGroup: builder.mutation({
            query: () => ({
                url: "/api/groups/quitGroup",
                method: "PUT",
            }),
        }),
    }),
});

export const { useJoinGroupMutation, useQuitGroupMutation } = groupApi;
