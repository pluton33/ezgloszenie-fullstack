import React, { useState } from "react";
import { LockIcon, MailIcon, SquareArrowRightEnter } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const API_BASE_URL = 'http://localhost:8080';

function Register(): React.JSX.Element {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        email: '',
        password: '',
        confirmPassword: '',
    });
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const setField = (name: 'email' | 'password' | 'confirmPassword', value: string) => {
        setForm(prev => ({ ...prev, [name]: value }));
        if (error) setError(null);
        if (success) setSuccess(null);
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!form.email.trim() || !form.password || !form.confirmPassword) {
            setError('Uzupełnij wszystkie pola.');
            return;
        }

        if (form.password !== form.confirmPassword) {
            setError('Hasła nie są takie same.');
            return;
        }

        setIsLoading(true);
        setError(null);
        setSuccess(null);

        try {
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: form.email.trim(),
                    passwordHash: form.password,
                    firstName: '',
                    lastName: '',
                }),
            });

            if (response.ok) {
                setSuccess('Konto zostało utworzone. Możesz się zalogować.');
                setForm({ email: '', password: '', confirmPassword: '' });
                setTimeout(() => navigate('/login'), 1000);
                return;
            }

            setError(`Nie udało się założyć konta. Kod błędu: ${response.status}.`);
        } catch {
            setError('Nie udało się połączyć z serwerem.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form className='login-content' onSubmit={handleSubmit}>
            <label className='login-text-label' htmlFor="username">Adres e-mail:</label>
            <div className='login-text'>
                <MailIcon className='login-icon' size={16} />
                <input
                    type='text'
                    className='login-text-input'
                    name="username"
                    id="username"
                    value={form.email}
                    onChange={(e) => setField('email', e.target.value)}
                />
            </div>
            <label className='login-text-label' style={{ marginTop: '20px' }} htmlFor="password">Hasło:</label>
            <div className='login-text' >
                <LockIcon className='login-icon' size={16} />
                <input
                    type='password'
                    className='login-text-input'
                    name="password"
                    id="password"
                    value={form.password}
                    onChange={(e) => setField('password', e.target.value)}
                />
            </div>

            <label className='login-text-label' style={{ marginTop: '20px' }} htmlFor="confirmpassword">Powtórz Hasło:</label>
            <div className='login-text' >
                <LockIcon className='login-icon' size={16} />
                <input
                    type='password'
                    className='login-text-input'
                    name="confirmpassword"
                    id="confirmpassword"
                    value={form.confirmPassword}
                    onChange={(e) => setField('confirmPassword', e.target.value)}
                />
            </div>

            {error && <div style={{ marginTop: '14px', color: '#c62828' }}>{error}</div>}
            {success && <div style={{ marginTop: '14px', color: '#2e7d32' }}>{success}</div>}

            <button className='login-btn' style={{ marginTop: '30px' }} type='submit' disabled={isLoading}>
                <span className='login-btn-text'>{isLoading ? 'Tworzenie...' : 'Załóż konto'}</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>

            <div className='login-lub' style={{ marginTop: '18px' }}>
                <hr className='login-hr' />
                <span className='login-text-label' >lub</span>
                <hr className='login-hr' />
            </div>

            <button className='login-btn' type='button' style={{ marginTop: '18px' }} onClick={() => navigate('/login')}>
                <span className='login-btn-text'> Zaloguj się</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>
        </form>
    )
}

export default Register