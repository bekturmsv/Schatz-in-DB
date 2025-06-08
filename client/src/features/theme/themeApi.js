import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const themeApi = createApi({
    reducerPath: 'themeApi',
    baseQuery: fetchBaseQuery({
        baseUrl: import.meta.env.VITE_API_BASE_URL,
        prepareHeaders: (headers, { getState }) => {
            const token = getState().auth.token;
            if (token) headers.set('Authorization', `Bearer ${token}`);
            return headers;
        },
    }),
    endpoints: (builder) => ({
        getThemes: builder.query({
            query: () => 'api/themes',
        }),
        purchaseTheme: builder.mutation({
            query: (themeName) => ({
                url: `api/themes/purchase`,
                method: "POST",
                params: { name: themeName }, // query string
            }),
        }),
    }),
});

export const { useGetThemesQuery, usePurchaseThemeMutation  } = themeApi;
