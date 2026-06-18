import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import type { MealInput } from '../types'
import { getMealById, createMeal, updateMeal } from '../api/macrosApi'

const EMPTY: MealInput = {
  name: '',
  calories: 0,
  protein: 0,
  fat: 0,
  carbs: 0,
  date: new Date().toISOString().split('T')[0],
  userId: null,
}

function MealForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEdit = Boolean(id)

  const [form, setForm] = useState<MealInput>(EMPTY)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (isEdit) {
      getMealById(Number(id))
        .then(data => setForm({
          name: data.name,
          calories: data.calories,
          protein: data.protein,
          fat: data.fat,
          carbs: data.carbs,
          date: data.date,
          userId: data.userId,
        }))
        .catch(() => navigate('/meals'))
    }
  }, [id, isEdit, navigate])

  function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
    const { name, value, type } = event.target
    setForm(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value,
    }))
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)
    try {
      if (isEdit) {
        await updateMeal(Number(id), form)
      } else {
        await createMeal(form)
      }
      navigate('/meals')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Fehler beim Speichern')
    }
  }

  return (
    <div>
      <h1>{isEdit ? 'Meal bearbeiten' : 'Neues Meal'}</h1>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit} className="meal-form">
        <div className="form-group">
          <label>Name</label>
          <input name="name" value={form.name} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Kalorien</label>
          <input name="calories" type="number" value={form.calories} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Protein (g)</label>
          <input name="protein" type="number" step="0.1" value={form.protein} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Fett (g)</label>
          <input name="fat" type="number" step="0.1" value={form.fat} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Kohlenhydrate (g)</label>
          <input name="carbs" type="number" step="0.1" value={form.carbs} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Datum</label>
          <input name="date" type="date" value={form.date} onChange={handleChange} required />
        </div>
        <button type="submit" className="btn btn-submit">
          {isEdit ? 'Speichern' : 'Erstellen'}
        </button>
      </form>
    </div>
  )
}

export default MealForm
