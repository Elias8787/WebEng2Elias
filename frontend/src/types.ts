export interface MealResponse {
  id: number
  name: string
  calories: number
  protein: number
  fat: number
  carbs: number
  date: string
  userId: number | null
}

export interface MealInput {
  name: string
  calories: number
  protein: number
  fat: number
  carbs: number
  date: string
  userId: number | null
}

export interface User {
  id: number
  username: string
  weight: number
  height: number
  age: number
  male: boolean
  palValue: number
  goal: string
  city: string
  dailyCalories: number
}

export interface UserInput {
  username: string
  weight: number
  height: number
  age: number
  male: boolean
  palValue: number
  goal: string
  city: string
}

export interface RecipeDTO {
  name: string
  image: string | null
  calories: number
  protein: number
  fat: number
  carbs: number
  ingredients: string[]
}

export interface WeatherDTO {
  city: string
  condition: string
  description: string
  temperature: number
}

export interface ActivitySuggestionDTO {
  id: number
  activityType: string
  estimatedCaloriesBurned: number
  weatherCondition: string
  userId: number
}
