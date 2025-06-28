import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const taskApi = createApi({
    reducerPath: 'taskApi',
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
    tagTypes: ['Tasks', 'Task', 'User'],
    endpoints: (builder) => ({
        getTopics: builder.query({
            query: (difficulty) => ({
                url: "api/task/getTopics",
                params: { schwierigkeit: difficulty },
            }),
        }),
        getTasksByTopic: builder.query({
            query: ({ difficulty, topicName }) => ({
                url: '/api/task/getByDifficult',
                params: { schwierigkeit: difficulty, sqlKategorie: topicName }
            }),
            providesTags: (result, error, arg) =>
                result
                    ? [
                        ...result.map(({ id }) => ({ type: 'Task', id })),
                        { type: 'Tasks', id: `${arg.difficulty}-${arg.topicName}` },
                    ]
                    : [{ type: 'Tasks', id: `${arg.difficulty}-${arg.topicName}` }],
        }),
        getTaskById: builder.query({
            query: ({ taskId }) => `api/task/getById/${taskId}`,
            providesTags: (result, error, arg) =>
                [{ type: 'Task', id: arg.taskId }],
        }),
        getLevels: builder.query({
            query: () => "api/task/getLevels",
        }),
        submitTaskAnswer: builder.mutation({
            query: ({ taskId, answer }) => ({
                url: `api/task/submit/${taskId}`,
                method: 'POST',
                body: { answer: JSON.stringify(answer) },
            }),
            invalidatesTags: (result, error, { taskId }) => [
                { type: 'Task', id: taskId },
                { type: 'Tasks' },
                { type: 'User' },
            ],
        }),
        validateSql: builder.mutation({
            query: ({ userSql, taskCode }) => ({
                url: "api/sql/validate",
                method: 'POST',
                body: { userSql, taskCode },
            }),
        }),
        getFinalTestByDifficulty: builder.query({
            query: (difficulty) => ({
                url: "/api/test/getTestByDifficulty",
                params: { schwierigkeitsgrad: difficulty },
            }),
        }),
        validateFinalTest: builder.mutation({
            query: ({ schwierigkeitsgrad, spentTimeInSeconds }) => ({
                url: "/api/sql/test/validate",
                method: "POST",
                body: { schwierigkeitsgrad, spentTimeInSeconds },
            }),
        }),
    }),
});

export const {
    useGetTopicsQuery,
    useGetTasksByTopicQuery,
    useGetTaskByIdQuery,
    useGetLevelsQuery,
    useSubmitTaskAnswerMutation,
    useValidateSqlMutation,
    useGetFinalTestByDifficultyQuery,
    useValidateFinalTestMutation,
} = taskApi;
