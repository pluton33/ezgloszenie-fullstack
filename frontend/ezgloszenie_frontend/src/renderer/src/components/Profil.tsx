import React, { useEffect, useMemo, useState } from 'react';
import {
    User,
    Mail,
    Shield,
    FileText,
    RefreshCw,
    AlertCircle,
    Eye,
    Info,
    ChevronRight
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import '../assets/profil.css';

import { API_BASE_URL } from '../config/api';

interface UserData {
    id?: number;
    role?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
}

interface Report {
    id: number;
    title: string;
    description: string;
    status?: string | null;
    user?: UserData;
}

interface ReportsResponse {
    reports: Report[];
}

function Profil(): React.JSX.Element {
    const navigate = useNavigate();
    const [reports, setReports] = useState<Report[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const fetchProfileData = async () => {
        setIsLoading(true);
        setError(null);

        try {
            const response = await fetch(`${API_BASE_URL}/reports/me`, {
                credentials: 'include',
            });

            if (response.status === 401) {
                throw new Error('Musisz się zalogować, aby zobaczyć swój profil.');
            }

            if (!response.ok) {
                throw new Error(`Błąd serwera: ${response.status}`);
            }

            const data: ReportsResponse = await response.json();
            setReports(data.reports || []);
        } catch (err: any) {
            setError(err?.message || 'Nie udało się pobrać danych profilu.');
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchProfileData();
    }, []);

    const user = useMemo<UserData | null>(() => {
        const firstReportWithUser = reports.find(report => report.user);
        return firstReportWithUser?.user || null;
    }, [reports]);

    const recentReports = useMemo(() => {
        return [...reports].slice(0, 3);
    }, [reports]);

    const initials =
        `${user?.firstName?.[0] || ''}${user?.lastName?.[0] || ''}`.trim() || 'U';

    return (
        <div className="profil-page">
            <div className="profil-header">
                <div>
                    <h1 className="profil-title">Mój profil</h1>
                    <p className="profil-subtitle">
                        Podgląd podstawowych informacji o koncie oraz Twoich ostatnich zgłoszeń.
                    </p>
                </div>

                <button
                    onClick={fetchProfileData}
                    disabled={isLoading}
                    className="profil-refresh-btn"
                >
                    <RefreshCw size={18} className={isLoading ? 'icon-spin' : ''} />
                    Odśwież
                </button>
            </div>

            {error && (
                <div className="error-box">
                    <AlertCircle size={22} />
                    <p>{error}</p>
                </div>
            )}

            {!error && (
                <div className="profil-layout">
                    <div className="profil-left-column">
                        <div className="profil-card profil-user-card">
                            <div className="profil-avatar">{initials}</div>

                            <div className="profil-user-content">
                                <div className="profil-user-head">
                                    <h2>
                                        {user
                                            ? `${user.firstName || ''} ${user.lastName || ''}`.trim() || 'Użytkownik systemu'
                                            : 'Użytkownik systemu'}
                                    </h2>
                                    <p>Twoje dane konta w systemie e-Zgłoszenie</p>
                                </div>

                                <div className="profil-user-info">
                                    <div className="profil-info-row">
                                        <span className="profil-info-label">
                                            <Mail size={16} />
                                            E-mail
                                        </span>
                                        <strong>{user?.email || '-'}</strong>
                                    </div>

                                    <div className="profil-info-row">
                                        <span className="profil-info-label">
                                            <Shield size={16} />
                                            Rola
                                        </span>
                                        <strong>{user?.role || '-'}</strong>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="profil-card">
                            <div className="profil-section-head">
                                <h3>Ostatnie zgłoszenia</h3>
                                <button
                                    className="profil-link-btn"
                                    onClick={() => navigate('/moje-zgloszenia')}
                                >
                                    Zobacz wszystkie <ChevronRight size={16} />
                                </button>
                            </div>

                            <div className="profil-reports-list">
                                {isLoading ? (
                                    <div className="profil-empty">Ładowanie danych...</div>
                                ) : recentReports.length === 0 ? (
                                    <div className="profil-empty">
                                        Nie masz jeszcze żadnych zgłoszeń w systemie.
                                    </div>
                                ) : (
                                    recentReports.map(report => (
                                        <div className="profil-report-item" key={report.id}>
                                            <div className="profil-report-main">
                                                <div className="profil-report-top">
                                                    <strong>#{report.id} · {report.title}</strong>
                                                    <span className={`profil-status-chip ${report.status ? 'active' : 'muted'}`}>
                                                        {report.status || 'Oczekujące'}
                                                    </span>
                                                </div>

                                                <p>{report.description || 'Brak opisu zgłoszenia.'}</p>
                                            </div>

                                            <button
                                                className="profil-mini-btn"
                                                onClick={() => navigate('/moje-zgloszenia')}
                                                title="Przejdź do moich zgłoszeń"
                                            >
                                                <Eye size={17} />
                                            </button>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>
                    </div>

                    <div className="profil-right-column">
                        <button
                            className="action-card secondary profil-stat-card profil-clickable-card"
                            onClick={() => navigate('/moje-zgloszenia')}
                            type="button"
                        >
                            <FileText size={30} className="card-icon" />
                            <h2>{reports.length}</h2>
                            <p>Łączna liczba zgłoszeń</p>
                        </button>

                        <div className="profil-card">
                            <div className="profil-section-head">
                                <h3>Informacje</h3>
                            </div>

                            <div className="profil-info-box">
                                <Info size={18} />
                                <p>
                                    Dane widoczne w tym widoku są pobierane z informacji przypisanych
                                    do Twoich zgłoszeń dostępnych w systemie.
                                </p>
                            </div>

                            <div className="profil-info-box soft">
                                <User size={18} />
                                <p>
                                    Pełną listę swoich spraw oraz ich statusy możesz sprawdzić
                                    w zakładce „Moje Zgłoszenia”.
                                </p>
                            </div>

                            <div className="profil-info-box soft">
                                <Shield size={18} />
                                <p>
                                    W przypadku problemów z obsługą systemu skorzystaj z sekcji
                                    „Pomoc i instrukcje”.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Profil;