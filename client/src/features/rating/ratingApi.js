import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const ratingApi = createApi({
    reducerPath: 'ratingApi',
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
        getRatings: builder.query({
            query: () => '/api/rating',
        }),
    }),
});

export const { useGetRatingsQuery } = ratingApi;
