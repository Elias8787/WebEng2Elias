import { useState } from 'react'
import type { RecipeDTO } from '../types'
import { searchRecipes } from '../api/macrosApi'

function RecipeSearch() {
  const [query, setQuery] = useState('')
  const [recipes, setRecipes] = useState<RecipeDTO[]>([])
  const [loading, setLoading] = useState(false)

  async function handleSearch(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (!query.trim()) return
    setLoading(true)
    try {
      const data = await searchRecipes(query)
      setRecipes(data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <h1>Rezeptsuche</h1>
      <form onSubmit={handleSearch} className="search-form">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="z.B. Pasta, Chicken, Salad..."
        />
        <button type="submit" className="btn btn-submit">Suchen</button>
      </form>
      {loading && <p>Suche...</p>}
      {recipes.length > 0 && (
        <div className="recipe-grid">
          {recipes.map((recipe, i) => (
            <div key={i} className="recipe-card">
              {recipe.image && <img src={recipe.image} alt={recipe.name} className="recipe-image" />}
              <h3>{recipe.name}</h3>
              <p>{recipe.calories} kcal | P: {recipe.protein}g | F: {recipe.fat}g | K: {recipe.carbs}g</p>
              <p className="ingredients"><strong>Zutaten:</strong> {recipe.ingredients?.join(', ')}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default RecipeSearch
