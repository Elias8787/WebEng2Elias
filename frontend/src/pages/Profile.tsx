import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import type { User, UserInput } from '../types'
import { getUsers, createUser, updateUser } from '../api/macrosApi'

const EMPTY: UserInput = {
  username: '',
  weight: 70,
  height: 175,
  age: 25,
  male: true,
  palValue: 1.5,
  goal: 'MAINTAIN',
  city: '',
}

function Profile() {
  const navigate = useNavigate()
  const [user, setUser] = useState<User | null>(null)
  const [form, setForm] = useState<UserInput>(EMPTY)

  useEffect(() => {
    let ignore = false
    getUsers().then(users => {
      if (!ignore && users.length > 0) {
        const u = users[0]
        setUser(u)
        setForm({
          username: u.username,
          weight: u.weight,
          height: u.height,
          age: u.age,
          male: u.male,
          palValue: u.palValue,
          goal: u.goal,
          city: u.city,
        })
      }
    })
    return () => { ignore = true }
  }, [])

  function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    const { name, value, type } = event.target
    setForm(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value,
    }))
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (user) {
      await updateUser(user.id, form)
    } else {
      await createUser(form)
    }
    navigate('/')
  }

  return (
    <div>
      <h1>{user ? 'Profil bearbeiten' : 'Profil erstellen'}</h1>
      <form onSubmit={handleSubmit} className="meal-form">
        <div className="form-group">
          <label>Username</label>
          <input name="username" value={form.username} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Gewicht (kg)</label>
          <input name="weight" type="number" step="0.1" value={form.weight} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Größe (cm)</label>
          <input name="height" type="number" step="0.1" value={form.height} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Alter</label>
          <input name="age" type="number" value={form.age} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Geschlecht</label>
          <div className="radio-group">
            <label>
              <input type="radio" name="male" checked={form.male === true}
                onChange={() => setForm(prev => ({ ...prev, male: true }))} />
              {' '}Männlich
            </label>
            <label>
              <input type="radio" name="male" checked={form.male === false}
                onChange={() => setForm(prev => ({ ...prev, male: false }))} />
              {' '}Weiblich
            </label>
          </div>
        </div>
        <div className="form-group">
          <label>Aktivitätslevel (PAL)</label>
          <select name="palValue" value={form.palValue} onChange={handleChange}>
            <option value={1.2}>Sehr wenig Bewegung (1.2)</option>
            <option value={1.4}>Wenig aktiv (1.4)</option>
            <option value={1.5}>Mäßig aktiv (1.5)</option>
            <option value={1.6}>Aktiv (1.6)</option>
            <option value={1.8}>Sehr aktiv (1.8)</option>
            <option value={2.0}>Leistungssportler (2.0)</option>
          </select>
        </div>
        <div className="form-group">
          <label>Ziel</label>
          <select name="goal" value={form.goal} onChange={handleChange}>
            <option value="LOSE">Abnehmen</option>
            <option value="MAINTAIN">Gewicht halten</option>
            <option value="GAIN">Zunehmen</option>
          </select>
        </div>
        <div className="form-group">
          <label>Stadt (für Wetter)</label>
          <input name="city" value={form.city} onChange={handleChange} required />
        </div>
        <button type="submit" className="btn btn-submit">
          {user ? 'Speichern' : 'Erstellen'}
        </button>
      </form>
    </div>
  )
}

export default Profile
