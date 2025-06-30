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
    tagTypes: ['User'],
    endpoints: (builder) => ({
        getThemes: builder.query({
            query: () => "/api/themes",
            providesTags: ['User'],
        }),
        purchaseTheme: builder.mutation({
            query: ({ name }) => ({
                url: `/api/themes/purchase?name=${encodeURIComponent(name)}`,
                method: "POST",
            }),
            transformResponse: (response, meta, arg) => {
                // Если ответ — строка, преобразуем в объект с сообщением
                if (typeof response === "string") {
                    return { message: response, user: null }; // Предполагаем, что user не вернулся
                }
                return response; // Возвращаем как есть, если это JSON
            },
            async onQueryStarted(args, { dispatch, queryFulfilled }) {
                try {
                    const { data } = await queryFulfilled;
                    if (data && data.user) {
                        console.log("Updated user data:", data.user);
                        dispatch(setUser(data.user));
                    } else {
                        console.log("No user data in response:", data);
                    }
                    dispatch(authApi.util.invalidateTags(['User']));
                } catch (error) {
                    console.error("Purchase theme error:", error);
                }
            },
            invalidatesTags: ['User'],
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