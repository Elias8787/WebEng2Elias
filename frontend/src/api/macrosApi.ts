import type { MealResponse, MealInput, User, UserInput, RecipeDTO, WeatherDTO, ActivitySuggestionDTO } from '../types'

const BASE = '/api'

async function handle<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(JSON.stringify(error))
  }
  return (await response.json()) as T
}

// Meals
export function getMeals(): Promise<MealResponse[]> {
  return fetch(`${BASE}/meals`).then(res => handle<MealResponse[]>(res))
}

export function getMealById(id: number): Promise<MealResponse> {
  return fetch(`${BASE}/meals/${id}`).then(res => handle<MealResponse>(res))
}

export function getMealsByUser(userId: number): Promise<MealResponse[]> {
  return fetch(`${BASE}/meals/user/${userId}`).then(res => handle<MealResponse[]>(res))
}

export function createMeal(meal: MealInput): Promise<MealResponse> {
  return fetch(`${BASE}/meals`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(meal),
  }).then(res => handle<MealResponse>(res))
}

export function updateMeal(id: number, meal: MealInput): Promise<MealResponse> {
  return fetch(`${BASE}/meals/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(meal),
  }).then(res => handle<MealResponse>(res))
}

export function deleteMeal(id: number): Promise<void> {
  return fetch(`${BASE}/meals/${id}`, { method: 'DELETE' }).then(res => {
    if (!res.ok) throw new Error(`HTTP ${res.status}`)
  })
}

// Users
export function getUsers(): Promise<User[]> {
  return fetch(`${BASE}/users`).then(res => handle<User[]>(res))
}

export function getUserById(id: number): Promise<User> {
  return fetch(`${BASE}/users/${id}`).then(res => handle<User>(res))
}

export function createUser(user: UserInput): Promise<User> {
  return fetch(`${BASE}/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user),
  }).then(res => handle<User>(res))
}

export function updateUser(id: number, user: UserInput): Promise<User> {
  return fetch(`${BASE}/users/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user),
  }).then(res => handle<User>(res))
}

export function getDailyCalories(userId: number): Promise<number> {
  return fetch(`${BASE}/users/${userId}/calories`).then(res => handle<number>(res))
}

// Recipes
export function searchRecipes(query: string): Promise<RecipeDTO[]> {
  return fetch(`${BASE}/recipes/search?query=${encodeURIComponent(query)}`).then(res => handle<RecipeDTO[]>(res))
}

// Weather
export function getWeather(city: string): Promise<WeatherDTO> {
  return fetch(`${BASE}/weather?city=${encodeURIComponent(city)}`).then(res => handle<WeatherDTO>(res))
}

export function getActivitySuggestions(userId: number): Promise<ActivitySuggestionDTO[]> {
  return fetch(`${BASE}/weather/activities/${userId}`).then(res => handle<ActivitySuggestionDTO[]>(res))
}
