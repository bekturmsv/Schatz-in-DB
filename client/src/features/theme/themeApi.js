// src/features/theme/themeApi.js
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { setUser } from "@/features/auth/authSlice";

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
            async onQueryStarted(args, { dispatch, queryFulfilled }) {
                try {
                    const { data } = await queryFulfilled;
                    console.log(data)
                    if (data && data.user) {
                        dispatch(setUser(data.user));
                    }
                } catch (error){
                    console.log(error)
                }
            },
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
