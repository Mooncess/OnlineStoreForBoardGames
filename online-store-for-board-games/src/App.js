import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import TestPage from './pages/TestPage';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import ProfilePage from './pages/ProfilePage';
import ProtectedRoute from './utils/ProtectedRoute';
import checkAuthStatus from './utils/checkAuthStatus';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/test" element={<TestPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/register" element={<RegistrationPage/>} />
          <Route path="/profile" element={<ProfilePage/>} />
        </Routes>
    </Router>
  );
}

export default App;
