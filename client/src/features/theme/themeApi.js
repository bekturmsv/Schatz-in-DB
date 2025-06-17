import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const themeApi = createApi({
    reducerPath: "themeApi",
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
        getThemes: builder.query({
            query: () => "/api/themes",
        }),
        purchaseTheme: builder.mutation({
            query: ({ name }) => ({
                url: `/api/themes/purchase?name=${encodeURIComponent(name)}`,
                method: "POST",
            }),
        }),
        setTheme: builder.mutation({
            query: ({ name }) => ({
                url: `/api/themes/setTheme?name=${encodeURIComponent(name)}`,
                method: "POST",
            }),
        }),
    }),
});

export const {
    useGetThemesQuery,
    usePurchaseThemeMutation,
    useSetThemeMutation,
} = themeApi;
