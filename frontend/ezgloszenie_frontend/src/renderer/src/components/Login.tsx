import React from 'react'
import { LockIcon, MailIcon, SquareArrowRightEnter } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

function Login(): React.JSX.Element {
    const navigate = useNavigate();


    return (
        <div className='login-content'>
            <label className='login-text-label' htmlFor="username">Adres e-mail:</label>
            <div className='login-text'>
                <MailIcon className='login-icon' size={16} />
                <input type='text' className='login-text-input' name="username" />
            </div>
            <label className='login-text-label' style={{ marginTop: '20px' }} htmlFor="password">Hasło:</label>
            <div className='login-text' >
                <LockIcon className='login-icon' size={16} />
                <input type='password' className='login-text-input' name="password" />
            </div>

            <button className='login-btn' onClick={() => navigate('/home')} style={{ marginTop: '30px' }} >
                <span className='login-btn-text'>Zaloguj Się</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>

            <div className='login-lub' style={{ marginTop: '18px' }}>
                <hr className='login-hr' />
                <span className='login-text-label' >lub</span>
                <hr className='login-hr' />
            </div>

            <button className='login-btn' style={{ marginTop: '18px' }} onClick={() => navigate('/register')}>
                <span className='login-btn-text'>Załóż konto</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>

        </div>
    )
}

export default Login