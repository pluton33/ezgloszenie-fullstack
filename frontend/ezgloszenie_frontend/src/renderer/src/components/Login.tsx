import React, { useState } from 'react'
import { LockIcon, MailIcon, SquareArrowRightEnter } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const API_BASE_URL = '/api';

function Login(): React.JSX.Element {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        username: '',
        password: '',
    });
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const setField = (name: 'username' | 'password', value: string) => {
        setForm(prev => ({ ...prev, [name]: value }));
        if (error) setError(null);
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!form.username.trim() || !form.password.trim()) {
            setError('Uzupełnij adres e-mail i hasło.');
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            const body = new URLSearchParams();
            body.append('username', form.username.trim());
            body.append('password', form.password);

            const response = await fetch(`${API_BASE_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: body.toString(),
                credentials: 'include',
            });

            if (response.ok) {
                localStorage.setItem('userEmail', form.username.trim());
                navigate('/home');
                return;
            }

            if (response.status === 401) {
                setError('Nieprawidłowy e-mail lub hasło.');
                return;
            }

            setError(`Nie udało się zalogować. Kod błędu: ${response.status}.`);
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
                    value={form.username}
                    onChange={(e) => setField('username', e.target.value)}
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

            {error && <div style={{ marginTop: '14px', color: '#c62828' }}>{error}</div>}

            <button className='login-btn' style={{ marginTop: '30px' }} type='submit' disabled={isLoading}>
                <span className='login-btn-text'>{isLoading ? 'Logowanie...' : 'Zaloguj Się'}</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>

            <div className='login-lub' style={{ marginTop: '18px' }}>
                <hr className='login-hr' />
                <span className='login-text-label' >lub</span>
                <hr className='login-hr' />
            </div>

            <button className='login-btn' type='button' style={{ marginTop: '18px' }} onClick={() => navigate('/register')}>
                <span className='login-btn-text'>Załóż konto</span>
                <SquareArrowRightEnter className='key-icon' size={16} />
            </button>
        </form>
    )
}

export default Login