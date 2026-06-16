import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import MealList from './pages/MealList'
import MealDetail from './pages/MealDetail'
import MealForm from './pages/MealForm'
import RecipeSearch from './pages/RecipeSearch'
import Profile from './pages/Profile'
import './App.css'

function App() {
  return (
    <BrowserRouter>
      <nav className="navbar">
        <Link to="/" className="nav-brand">Macros</Link>
        <div className="nav-links">
          <Link to="/">Dashboard</Link>
          <Link to="/meals">Meals</Link>
          <Link to="/meals/new">Meal hinzufügen</Link>
          <Link to="/recipes">Rezeptsuche</Link>
          <Link to="/profile">Profil</Link>
        </div>
      </nav>
      <main className="container">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/meals" element={<MealList />} />
          <Route path="/meals/new" element={<MealForm />} />
          <Route path="/meals/:id" element={<MealDetail />} />
          <Route path="/meals/:id/edit" element={<MealForm />} />
          <Route path="/recipes" element={<RecipeSearch />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
