import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import TestPage from './pages/TestPage';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import ProfilePage from './pages/ProfilePage';
import AdminPanelPage from './pages/AdminPanelPage';
import ArtilceManagementPage from './pages/ArticleManagementPage';
import CategoryManagementPage from './pages/CategoryManagementPage';
import OrderManagementPage from './pages/OrderManagementPage';
import UserManagementPage from './pages/UserManagementPage';
import CreateArticlePage from './pages/CreateArticlePage';
import CatalogPage from './pages/CatalogPage';
import ArticlePage from './pages/ArtilcePage';
import WishListPage from './pages/WishListPage';
import BasketPage from './pages/BasketPage';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/test" element={<TestPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/registration" element={<RegistrationPage/>} />
          <Route path="/profile" element={<ProfilePage/>} />
          <Route path="/admin/admin-panel" element={<AdminPanelPage/>} />
          <Route path="/admin/article-management" element={<ArtilceManagementPage/>} />
          <Route path="/admin/category-management" element={<CategoryManagementPage/>} />
          <Route path="/admin/order-management" element={<OrderManagementPage/>} />
          <Route path="/admin/user-management" element={<UserManagementPage/>} />
          <Route path="/admin/create-article" element={<CreateArticlePage/>} />
          <Route path="/" element={<CatalogPage/>} />
          <Route path="/article/:id" element={<ArticlePage/>} />
          <Route path="/wish-list" element={<WishListPage/>} />
          <Route path="/basket" element={<BasketPage/>} />
        </Routes>
    </Router>
  );
}

export default App;
