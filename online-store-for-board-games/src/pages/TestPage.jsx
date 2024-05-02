import React from 'react';
import Header from '../components/MyNavbar';
import Footer from '../components/MyFooter';

const TestPage = () => {
    return (
        <div>
            <Header />
            <div>
                <h2>Main Content Goes Here</h2>
                <p>This is where your main content would be displayed.</p>
            </div>
            <Footer />
        </div>
    );
};

export default TestPage;