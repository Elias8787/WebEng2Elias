import { useEffect, useState } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import type { MealResponse } from '../types'
import { getMealById } from '../api/macrosApi'

function MealDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [meal, setMeal] = useState<MealResponse | null>(null)

  useEffect(() => {
    let ignore = false
    getMealById(Number(id))
      .then(data => { if (!ignore) setMeal(data) })
      .catch(() => navigate('/meals'))
    return () => { ignore = true }
  }, [id, navigate])

  if (!meal) return <p>Laden...</p>

  return (
    <div className="meal-detail">
      <h1>{meal.name}</h1>
      <div className="detail-grid">
        <div className="detail-card">
          <span className="label">Kalorien</span>
          <span className="value">{meal.calories} kcal</span>
        </div>
        <div className="detail-card">
          <span className="label">Protein</span>
          <span className="value">{meal.protein}g</span>
        </div>
        <div className="detail-card">
          <span className="label">Fett</span>
          <span className="value">{meal.fat}g</span>
        </div>
        <div className="detail-card">
          <span className="label">Kohlenhydrate</span>
          <span className="value">{meal.carbs}g</span>
        </div>
        <div className="detail-card">
          <span className="label">Datum</span>
          <span className="value">{meal.date}</span>
        </div>
      </div>
      <div className="detail-actions">
        <Link to={`/meals/${meal.id}/edit`} className="btn btn-edit">Bearbeiten</Link>
        <Link to="/meals" className="btn">Zurück</Link>
      </div>
    </div>
  )
}

export default MealDetail
