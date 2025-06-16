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
            query: (difficulty) => ({
                url: "api/task/getTopics",
                params: {schwierigkeit: difficulty},
            })
        }),
        getTasksByTopic: builder.query({
            query: ({ difficulty, topicName }) =>
                ({
                    url: '/api/task/getByDifficult',
                    params: {
                        schwierigkeit: difficulty,
                        sqlKategorie: topicName
                    }
                })
        }),
        getTaskById: builder.query({
            query: ({  taskId }) =>
                `api/task/getById/${taskId}`,
        }),
        getLevels: builder.query({
            query: () => "api/task/getLevels"
        }),
        submitTaskAnswer: builder.mutation({
            query: ({ taskId, answer }) => ({
                url: `api/task/submit/${taskId}`,
                method: 'POST',
                body: { answer: JSON.stringify(answer) },
            }),
        }),
        validateSql: builder.mutation({
            query: ({userSql, taskCode})=> ({
                url: "api/sql/validate",
                method: 'POST',
                body: { userSql, taskCode }
            })
        })

    }),
});

export const {
    useGetTopicsQuery,
    useGetTasksByTopicQuery,
    useGetTaskByIdQuery,
    useGetLevelsQuery,
    useSubmitTaskAnswerMutation,
    useValidateSqlMutation
} = taskApi;
