// src/store/taskApi.js
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const taskApi = createApi({
    reducerPath: 'taskApi',
    baseQuery: fetchBaseQuery({
        baseUrl: import.meta.env.VITE_API_BASE_URL,
        prepareHeaders: (headers, { getState }) => {
            // Получаем токен из store (RTK Query сам передаёт getState)
            const token = getState().auth.token;
            if (token) {
                headers.set('Authorization', `Bearer ${token}`);
            }
            return headers;
        },
    }),
    endpoints: (builder) => ({
        getTopics: builder.query({
            query: (difficulty) => `api/task/${difficulty.toUpperCase()}/topics`,
        }),
        getTasksByTopic: builder.query({
            // Меняем topicId на topicName и путь на ваш:
            query: ({ difficulty, topicName }) =>
                `api/task/${difficulty}/${topicName}/tasks`,
        }),
        getTaskById: builder.query({
            query: ({ difficulty, topicName, taskId }) =>
                `api/task/${difficulty.toUpperCase()}/${topicName}/tasks/${taskId}`,
        }),
        submitTaskAnswer: builder.mutation({
            query: ({ taskId, answer }) => ({
                url: `api/task/submit/${taskId}`,
                method: 'POST',
                body: { answer: JSON.stringify(answer) },
            }),
        }),
    }),
});

export const {
    useGetTopicsQuery,
    useGetTasksByTopicQuery,
    useGetTaskByIdQuery,
    useSubmitTaskAnswerMutation
} = taskApi;
