import React, { useState, useEffect } from 'react';
import { Eye, XCircle, AlertCircle, RefreshCw } from 'lucide-react';
import '../assets/mojezgloszenia.css';

const API_BASE_URL = 'http://34.116.134.38:8080';

export interface User {
  id?: number;
  role: 'USER' | 'MODERATOR' | 'ADMIN';
  email: string;
  badgeNumber?: number;
  firstName: string;
  lastName: string;
}

export interface Report {
  id: number;
  title: string;
  description: string;
  status?: string | null;
  user: User;
}

export interface ReportsResponse {
  reports: Report[];
}

const MojeZgloszenia = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchReports = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/reports/me`, {
        credentials: 'include',
      });

      if (response.status === 401) {
        throw new Error('Musisz się zalogować, aby zobaczyć swoje zgłoszenia.');
      }

      if (!response.ok) {
        throw new Error(`Błąd serwera: ${response.status}`);
      }

      const data: ReportsResponse = await response.json();
      setReports(data.reports || []);
    } catch (err: any) {
      console.error('Błąd podczas pobierania zgłoszeń:', err);
      setError(err.message || 'Nie udało się pobrać danych z serwera.');
    } finally {
      setIsLoading(false);
    }
  };

  const deleteReport = async (id: number) => {
    try {
      const response = await fetch(`${API_BASE_URL}/reports/${id}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Nie udało się usunąć zgłoszenia. (${response.status})`);
      }

      setReports((prev) => prev.filter((report) => report.id !== id));
    } catch (err: any) {
      setError(err.message || 'Nie udało się usunąć zgłoszenia.');
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  return (
    <div className="zgloszenia-container">
      <div className="zgloszenia-header">
        <div>
          <h1 className="zgloszenia-title">Lista Zgłoszeń</h1>
          <p className="zgloszenia-subtitle">Przeglądaj swoje zgłoszenia</p>
        </div>
        <button
          onClick={fetchReports}
          disabled={isLoading}
          className="btn-refresh"
        >
          <RefreshCw size={18} className={isLoading ? 'icon-spin' : ''} />
          Odśwież
        </button>
      </div>

      {error && (
        <div className="error-box">
          <AlertCircle size={24} />
          <p>{error}</p>
        </div>
      )}

      <div className="table-wrapper">
        <table className="zgloszenia-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tytuł</th>
              <th>Krótki opis</th>
              <th>Zgłaszający</th>
              <th className="text-center">Akcje</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? (
              <tr>
                <td colSpan={5} className="table-message">
                  <RefreshCw className="icon-spin text-blue" size={28} />
                  <span>Ładowanie zgłoszeń z bazy danych...</span>
                </td>
              </tr>
            ) : reports.length === 0 && !error ? (
              <tr>
                <td colSpan={5} className="table-message">
                  Brak zgłoszeń w bazie danych.
                </td>
              </tr>
            ) : (
              reports.map((report) => (
                <tr key={report.id} className="table-row">
                  <td className="font-bold text-blue">#{report.id}</td>
                  <td className="font-medium">{report.title}</td>
                  <td className="truncate-text" title={report.description}>
                    {report.description}
                  </td>
                  <td>
                    <div className="user-info">
                      <span className="user-name">
                        {report.user?.firstName || '-'} {report.user?.lastName || ''}
                      </span>
                      <span className="user-badge">
                        {typeof report.user?.badgeNumber === 'number'
                          ? `Odznaka: ${report.user.badgeNumber} (${report.user?.role || '-'})`
                          : `${report.user?.role || '-'} · ${report.user?.email || ''}`}
                      </span>
                    </div>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button className="btn-action view" title="Zobacz szczegóły">
                        <Eye size={20} />
                      </button>
                      <button className="btn-action delete" title="Usuń" onClick={() => deleteReport(report.id)}>
                        <XCircle size={20} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MojeZgloszenia;