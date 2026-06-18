import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { User, MealResponse, WeatherDTO, ActivitySuggestionDTO } from '../types'
import { getUsers, getMealsByUser, getWeather, getActivitySuggestions, getDailyCalories } from '../api/macrosApi'

function Dashboard() {
  const [user, setUser] = useState<User | null>(null)
  const [weather, setWeather] = useState<WeatherDTO | null>(null)
  const [activities, setActivities] = useState<ActivitySuggestionDTO[]>([])
  const [todayMeals, setTodayMeals] = useState<MealResponse[]>([])
  const [dailyCalories, setDailyCalories] = useState(0)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let ignore = false
    getUsers()
      .then(users => {
        if (!ignore && users.length > 0) {
          const u = users[0]
          setUser(u)
          loadDashboardData(u)
        }
      })
      .finally(() => { if (!ignore) setLoading(false) })
    return () => { ignore = true }
  }, [])

  const loadDashboardData = async (u: User) => {
    try {
      const [weatherRes, activitiesRes, caloriesRes, mealsRes] = await Promise.all([
        getWeather(u.city),
        getActivitySuggestions(u.id),
        getDailyCalories(u.id),
        getMealsByUser(u.id),
      ])
      setWeather(weatherRes)
      setActivities(activitiesRes)
      setDailyCalories(caloriesRes)

      const today = new Date().toISOString().split('T')[0]
      setTodayMeals(mealsRes.filter(m => m.date === today))
    } catch (err) {
      console.error(err)
    }
  }

  const caloriesEaten = todayMeals.reduce((sum, m) => sum + m.calories, 0)
  const remaining = Math.round(dailyCalories - caloriesEaten)

  if (loading) return <p>Lade Dashboard...</p>

  if (!user) {
    return (
      <div className="welcome-card">
        <h1>Willkommen bei Macros!</h1>
        <p>Erstelle zuerst ein Profil um loszulegen.</p>
        <Link to="/profile" className="btn btn-submit">Profil erstellen</Link>
      </div>
    )
  }

  return (
    <div>
      <h1>Hallo, {user.username}!</h1>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <h2>Kalorien heute</h2>
          <div className="calorie-overview">
            <div className="calorie-item">
              <span className="label">Ziel</span>
              <span className="value">{Math.round(dailyCalories)} kcal</span>
            </div>
            <div className="calorie-item">
              <span className="label">Gegessen</span>
              <span className="value">{caloriesEaten} kcal</span>
            </div>
            <div className="calorie-item">
              <span className="label">Übrig</span>
              <span className={`value ${remaining >= 0 ? 'positive' : 'negative'}`}>
                {remaining} kcal
              </span>
            </div>
          </div>
        </div>

        {weather && (
          <div className="dashboard-card">
            <h2>Wetter in {weather.city}</h2>
            <p className="weather-temp">{Math.round(weather.temperature)}°C</p>
            <p>{weather.description}</p>
          </div>
        )}

        {activities.length > 0 && (
          <div className="dashboard-card">
            <h2>Aktivitätsvorschläge</h2>
            <ul className="activity-list">
              {activities.map((a, i) => (
                <li key={i}>
                  <strong>{a.activityType}</strong> – ca. {a.estimatedCaloriesBurned} kcal/h
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>

      {todayMeals.length > 0 && (
        <div className="dashboard-card today-card">
          <h2>Heutige Meals</h2>
          <ul className="today-meals">
            {todayMeals.map(m => (
              <li key={m.id}>
                <Link to={`/meals/${m.id}`}>{m.name}</Link> – {m.calories} kcal
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}

export default Dashboard
