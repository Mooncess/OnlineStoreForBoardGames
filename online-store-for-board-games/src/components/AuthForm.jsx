import React, { useState } from 'react';

const AuthForm = ({ formType, onAuth }) => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        phoneNumber: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = () => {
        onAuth(formData, formType);
    };

    return (
        <div>
            <input type="email" name="email" value={formData.username} onChange={handleChange} placeholder="Email" />
            <input type="password" name="password" value={formData.password} onChange={handleChange} placeholder="Password" />
            {formType === 'register' && (
                <>
                    <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} placeholder="First Name" />
                    <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} placeholder="Last Name" />
                    <input type="tel" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} placeholder="Phone Number" />
                </>
            )}
            <button onClick={handleSubmit}>{formType === 'login' ? 'Login' : 'Register'}</button>
        </div>
    );
};

export default AuthForm;