import { apiClient } from "./apiService";


export const jobService = {

    getAllJobs: async () => {
        const response = await apiClient.get('/api/v1/jobs');
        return response.data;
    },

    getJob: async (id) => {
        const response = await apiClient.get(`/api/v1/jobs/${id}`);
        return response.data;
    },

    registerNewJob: async (request) => {
        const response = await apiClient.post(`/api/v1/jobs/register`, request);
        return response.data;
    },

    updateJob: async (id, job) => {
        const response = await apiClient.put(`/api/v1/jobs/${id}`, job);
        return response.data;
    },

    deleteJob: async () => {
        const response = await apiClient.delete(`/api/v1/jobs/${id}`);
        return response.data;
    },
}