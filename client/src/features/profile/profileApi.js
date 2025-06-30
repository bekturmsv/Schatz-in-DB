// features/profile/profileApi.js
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { setUser } from "@/features/auth/authSlice";

export const profileApi = createApi({
    reducerPath: "profileApi",
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
    tagTypes: ['User'],
    endpoints: (builder) => ({
        updatePlayer: builder.mutation({
            query: ({ id, data }) => ({
                url: `/api/profile/player/update/${id}`,
                method: "PUT",
                body: data,
            }),
            async onQueryStarted({ id, data }, { dispatch, queryFulfilled }) {
                try {
                    const { data: user } = await queryFulfilled;
                    dispatch(setUser(user));
                } catch (e) {}
            },
            invalidatesTags: [{ type: 'User' }],
        }),
        joinGroup: builder.mutation({
            query: ({ groupCode }) => ({
                url: `/api/groups/changeGroup`,
                method: "POST",
                body: { groupCode },
            }),
            async onQueryStarted(_, { dispatch, queryFulfilled }) {
                try {
                    const { data: user } = await queryFulfilled;
                    dispatch(setUser(user));
                } catch (e) {}
            },
            invalidatesTags: [{ type: 'User' }],
        }),
        quitGroup: builder.mutation({
            query: () => ({
                url: `/api/groups/quitGroup`,
                method: "POST",
            }),
            async onQueryStarted(_, { dispatch, queryFulfilled }) {
                try {
                    const { data: user } = await queryFulfilled;
                    dispatch(setUser(user));
                } catch (e) {}
            },
            invalidatesTags: [{ type: 'User' }],
        }),
    }),
});

export const { useUpdatePlayerMutation, useJoinGroupMutation, useQuitGroupMutation } = profileApi;
