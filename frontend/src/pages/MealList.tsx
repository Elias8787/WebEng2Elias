import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { MealResponse } from '../types'
import { getMeals, deleteMeal } from '../api/macrosApi'

function MealList() {
  const [meals, setMeals] = useState<MealResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let ignore = false
    getMeals()
      .then(data => { if (!ignore) setMeals(data) })
      .finally(() => { if (!ignore) setLoading(false) })
    return () => { ignore = true }
  }, [])

  const handleDelete = async (id: number) => {
    await deleteMeal(id)
    setMeals(meals.filter(m => m.id !== id))
  }

  if (loading) return <p>Lade Meals...</p>

  return (
    <div>
      <h1>Meals</h1>
      {meals.length === 0 ? (
        <p>Keine Meals vorhanden. <Link to="/meals/new">Erstelle eins!</Link></p>
      ) : (
        <table className="meal-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Kalorien</th>
              <th>Protein</th>
              <th>Fett</th>
              <th>Kohlenhydrate</th>
              <th>Datum</th>
              <th>Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {meals.map(meal => (
              <tr key={meal.id}>
                <td><Link to={`/meals/${meal.id}`}>{meal.name}</Link></td>
                <td>{meal.calories} kcal</td>
                <td>{meal.protein}g</td>
                <td>{meal.fat}g</td>
                <td>{meal.carbs}g</td>
                <td>{meal.date}</td>
                <td>
                  <Link to={`/meals/${meal.id}/edit`} className="btn btn-edit">Bearbeiten</Link>
                  <button onClick={() => handleDelete(meal.id)} className="btn btn-delete">Löschen</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default MealList
