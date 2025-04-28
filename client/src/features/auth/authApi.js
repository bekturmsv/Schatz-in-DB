import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import api from "../../api/axiosConfig";

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: fetchBaseQuery({
    baseUrl: api.defaults.baseURL,
    prepareHeaders: (headers) => {
      // You can add headers if you need to
      return headers;
    },
    /**
     * The function that will be used to make requests to the api.
     * @param {string} input - The url to make the request to.
     * @param {object} init - The options to pass to the fetch function.
     * @returns {Promise} - A promise that resolves to the response data.
     */
    fetchFn: async (input, init) => {
      const response = await api({
        url: input,
        method: init.method || "GET",
        data: init.body,
        headers: init.headers,
      });
      return { data: response.data };
    },
  }),
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (credentials) => ({
        url: "/login",
        method: "POST",
        body: credentials,
      }),
    }),
    register: builder.mutation({
      query: (userData) => ({
        url: "/register",
        method: "POST",
        body: userData,
      }),
    }),
  }),
});

export const { useLoginMutation, useRegisterMutation } = authApi;
