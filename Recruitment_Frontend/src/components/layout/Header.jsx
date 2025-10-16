import { Link } from 'react-router-dom'

export default function Header() {
  return (
    <header className="bg-white shadow">
      <nav className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="text-2xl font-bold">
            My App
          </Link>
          <div className="flex gap-4">
            <Link to="/" className="hover:text-blue-600">
              Home
            </Link>
            <Link to="/about" className="hover:text-blue-600">
              About
            </Link>
          </div>
        </div>
      </nav>
    </header>
  )
}