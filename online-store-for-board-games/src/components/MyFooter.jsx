// Footer.js
import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faFacebook, faTwitter, faInstagram } from '@fortawesome/free-brands-svg-icons'
import '../styles/MyFooter.css';

const MyFooter = () => {
    return (
        <footer className="footer">
            <div>
                <p>&copy; {new Date().getFullYear()} Board Game Emporium</p>
                <p>Follow Us:</p>
                <div className="social-icons">
                    <a href="https://facebook.com"><FontAwesomeIcon icon={faFacebook} /></a>
                    <a href="https://twitter.com"><FontAwesomeIcon icon={faTwitter} /></a>
                    <a href="https://instagram.com"><FontAwesomeIcon icon={faInstagram} /></a>
                </div>
            </div>
        </footer>
    );
};

export default MyFooter;