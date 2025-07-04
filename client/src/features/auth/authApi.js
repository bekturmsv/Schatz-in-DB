import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { setUser, setToken } from "./authSlice";

export const authApi = createApi({
  reducerPath: "authApi",
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
    login: builder.mutation({
      query: (credentials) => ({
        url: "/api/auth/login",
        method: "POST",
        body: credentials,
      }),
      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          if (data.token) dispatch(setToken(data.token));
          if (data.user) dispatch(setUser(data.user));
        } catch (error) {
          throw error;
        }
      },
      invalidatesTags: [{ type: 'User' }],
    }),
    register: builder.mutation({
      query: (userData) => ({
        url: "/api/auth/register",
        method: "POST",
        body: userData,
      }),
      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          if (data.token) dispatch(setToken(data.token));
          if (data.user) dispatch(setUser(data.user));
        } catch (error) {
          throw error;
        }
      },
      invalidatesTags: [{ type: 'User' }],
    }),
    getSpecializations: builder.query({
      query: () => ({
        url: "/api/specialist/getAll",
        method: "GET",
      }),
    }),
    getMe: builder.query({
      query: () => ({
        url: "/api/profile/player/getAuthorizedUser",
        method: "GET",
      }),
      transformResponse: (response) => response,
      providesTags: ['User'],
    }),
  }),
});

export const {
  useLoginMutation,
  useRegisterMutation,
  useGetSpecializationsQuery,
  useGetMeQuery,
} = authApi;
