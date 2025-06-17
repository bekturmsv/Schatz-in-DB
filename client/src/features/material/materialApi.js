import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const materialApi = createApi({
    reducerPath: 'materialApi',
    baseQuery: fetchBaseQuery({
        baseUrl: import.meta.env.VITE_API_BASE_URL,
        prepareHeaders: (headers, { getState }) => {
            const token = getState().auth.token;
            if (token) {
                headers.set('Authorization', `Bearer ${token}`);
            }
            return headers;
        },
    }),
    endpoints: (builder) => ({
        getMaterials: builder.query({
            query: () => '/api/materials',
        }),
        getMaterialById: builder.query({
            query: (id) => `/api/materials/${id}`,
        }),
    }),
});

export const { useGetMaterialsQuery, useGetMaterialByIdQuery } = materialApi;
