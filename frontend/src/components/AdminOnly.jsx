import { useAuth } from '../context/authContext'

const AdminOnly = ({ children }) => {
  const { isAdmin } = useAuth()
  return isAdmin ? children : null
}

export default AdminOnly
