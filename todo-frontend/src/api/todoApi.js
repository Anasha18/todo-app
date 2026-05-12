import { axiosInstance } from './baseApi.js'

export const getTodosPagination = async (page = 0, size = 10) => {
    const response = await axiosInstance.get('/todos', {
        params: {
            page,
            size,
        },
    })
    return response.data
}

export const getTodoById = async (id) => {
    const response = await axiosInstance.get(`/todos/${id}`)
    return response.data
}

export const createTodo = async (todo) => {
    const response = await axiosInstance.post('/todos', todo)
    return response.data
}

export const updateTodo = async (id, todo) => {
    const response = await axiosInstance.put(`/todos/${id}`, todo)
    return response.data
}

export const completeTodo = async (id) => {
    const response = await axiosInstance.put(`/todos/complete/${id}`)
    return response.data
}

export const deleteTodo = async (id) => {
    const response = await axiosInstance.delete(`/todos/${id}`)
    return response.data
}