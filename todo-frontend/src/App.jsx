import {useCallback, useEffect, useState} from 'react'
import './App.css'
import './index.css'

import {
    getTodosPagination,
    createTodo,
    updateTodo,
    completeTodo,
    deleteTodo,
} from './api/todoApi.js'

function App() {
    const [todos, setTodos] = useState([])

    const [name, setName] = useState('')
    const [description, setDescription] = useState('')

    const [page, setPage] = useState(0)
    const [size] = useState(10)

    const [editingId, setEditingId] = useState(null)

    const loadTodos = useCallback(async () => {
        const data = await getTodosPagination(page, size)

        setTodos(data)

    }, [page, size])

    useEffect(() => {
        void loadTodos()
    }, [loadTodos])

    async function saveTodo() {
        if (!name.trim() || !description.trim()) {
            alert('Заполни название и описание')
            return
        }

        const dto = {
            name,
            description,
        }

        if (editingId === null) {
            await createTodo(dto)
        } else {
            await updateTodo(editingId, dto)
        }

        setName('')
        setDescription('')
        setEditingId(null)

        await loadTodos()
    }

    async function handleDeleteTodo(id) {
        await deleteTodo(id)
        await loadTodos()
    }

    async function handleCompleteTodo(id) {
        await completeTodo(id)
        await loadTodos()
    }

    function startEdit(todo) {
        setEditingId(todo.id)
        setName(todo.name)
        setDescription(todo.description)
    }

    function cancelEdit() {
        setEditingId(null)
        setName('')
        setDescription('')
    }

    return (
        <div className="app">
            <div className="todo-page">
                <header className="todo-header">
                    <div>
                        <p className="todo-subtitle">Todo App</p>
                        <h1>Мои задачи</h1>
                    </div>

                    <button className="header-button" onClick={cancelEdit}>
                        + Новая задача
                    </button>
                </header>

                <section className="todo-layout">
                    <aside className="todo-form-card">
                        <h2>{editingId === null ? 'Создать задачу' : 'Изменить задачу'}</h2>

                        <p className="form-description">
                            Заполни название и описание задачи.
                        </p>

                        <form className="todo-form">
                            <div className="form-group">
                                <label htmlFor="name">Название</label>
                                <input
                                    id="name"
                                    type="text"
                                    value={name}
                                    onChange={(event) => setName(event.target.value)}
                                    placeholder="Например: Выучить Spring Boot"
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="description">Описание</label>
                                <textarea
                                    id="description"
                                    value={description}
                                    onChange={(event) => setDescription(event.target.value)}
                                    placeholder="Кратко опиши, что нужно сделать..."
                                />
                            </div>

                            <button type="button" className="submit-button" onClick={saveTodo}>
                                {editingId === null ? 'Сохранить' : 'Обновить'}
                            </button>

                            {editingId !== null && (
                                <button type="button" className="submit-button" onClick={cancelEdit}>
                                    Отмена
                                </button>
                            )}
                        </form>
                    </aside>

                    <main className="todo-content">
                        <div className="content-header">
                            <div>
                                <h2>Список задач</h2>
                                <p>Всего задач на странице: {todos.length}</p>
                            </div>
                        </div>

                        <div className="todo-list">
                            {todos.map((todo) => {
                                const completed = todo.isDone ?? todo.done

                                return (
                                    <article
                                        key={todo.id}
                                        className={completed ? 'todo-card todo-card-done' : 'todo-card'}
                                    >
                                        <div className="todo-card-header">
                                            <div>
                                                <h3>{todo.name}</h3>
                                                <p>ID: {todo.id}</p>
                                            </div>

                                            <span className={completed ? 'badge badge-done' : 'badge badge-progress'}>
                                                {completed ? 'Выполнено' : 'В процессе'}
                                            </span>
                                        </div>

                                        <p className="todo-description">
                                            {todo.description}
                                        </p>

                                        <div className="todo-meta">
                                            <span>
                                                Создано: {new Date(todo.createdAt).toLocaleString('ru-RU')}
                                            </span>
                                        </div>

                                        <div className="todo-actions">
                                            <button
                                                className="complete-button"
                                                disabled={completed}
                                                onClick={() => handleCompleteTodo(todo.id)}
                                            >
                                                {completed ? 'Выполнено' : 'Выполнить'}
                                            </button>

                                            <button
                                                className="edit-button"
                                                onClick={() => startEdit(todo)}
                                            >
                                                Изменить
                                            </button>

                                            <button
                                                className="delete-button"
                                                onClick={() => handleDeleteTodo(todo.id)}
                                            >
                                                Удалить
                                            </button>
                                        </div>
                                    </article>
                                )
                            })}
                        </div>

                        <div className="pagination">
                            <button
                                disabled={page === 0}
                                onClick={() => setPage(page - 1)}
                            >
                                Назад
                            </button>

                            <span>Страница {page + 1}</span>

                            <button onClick={() => setPage(page + 1)}>
                                Вперёд
                            </button>
                        </div>
                    </main>
                </section>
            </div>
        </div>
    )
}

export default App