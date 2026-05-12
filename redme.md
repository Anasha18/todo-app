# Todo-app

Сейчас в репозитории есть:

* Spring Boot 3.4 + Java 25 backend;
* Native PostgreSql
* endpoint-ы: `GET /api/v1/todos/` `POST /api/v1/todos/`
  `GET /api/v1/todos/id` `DELETE /api/v1/todos/id` `PUT /api/v1/todos/id`  `PUT /api/v1/todos/complete/id`

# Структура
* todo-backend/ — Spring Boot backend
* todo-frontend/ — React frontend
* docker-compose.yml — локальный PostgreSQL


# React Todo App

## useState

`useState` используется для хранения состояния:

```js
const [todos, setTodos] = useState([])
```

- `todos` — список задач
- `setTodos()` — обновление списка

Также используется для формы, пагинации и редактирования.

---

## useEffect

```js
useEffect(() => {
    void loadTodos()
}, [loadTodos])
```

Используется для загрузки задач при открытии страницы и смене страницы.

---

## useCallback

```js
const loadTodos = useCallback(async () => {
    ...
}, [page, size])
```

Запоминает функцию и предотвращает лишние перерисовки.

---

## axios

Используется для HTTP-запросов к backend API.

```js
await axiosInstance.get('/todos')
```

---

## async / await

Используется для асинхронных запросов.

```js
await createTodo(dto)
```

---

## map()

```js
todos.map((todo) => ...)
```

Используется для отображения списка задач.


## UI

![img.png](img.png)