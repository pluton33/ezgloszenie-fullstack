import React, { useEffect, useState } from 'react';
import {
  Eye,
  XCircle,
  AlertCircle,
  RefreshCw,
  X,
  User,
  Mail,
  FileText,
  MapPin,
  Clock,
  ShieldAlert,
  ClipboardList
} from 'lucide-react';
import '../assets/mojezgloszenia.css';

const API_BASE_URL = '/api';

export interface UserData {
  id?: number;
  role?: 'USER' | 'MODERATOR' | 'ADMIN' | string;
  email?: string;
  badgeNumber?: number;
  firstName?: string;
  lastName?: string;
}

export interface Category {
  id?: number;
  name?: string;
}

export interface Report {
  id: number;
  title: string;
  description: string;
  status?: string | null;
  location?: string;
  accident_date?: string;
  userAnonymous?: boolean;
  category?: Category | null;
  user?: UserData;
}

export interface ReportsResponse {
  reports: Report[];
}

const MojeZgloszenia = (): React.JSX.Element => {
  const [reports, setReports] = useState<Report[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedReport, setSelectedReport] = useState<Report | null>(null);
  const [isDetailsOpen, setIsDetailsOpen] = useState<boolean>(false);

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

      if (selectedReport?.id === id) {
        closeDetails();
      }
    } catch (err: any) {
      setError(err.message || 'Nie udało się usunąć zgłoszenia.');
    }
  };

  const openDetails = (report: Report) => {
    setSelectedReport(report);
    setIsDetailsOpen(true);
  };

  const closeDetails = () => {
    setIsDetailsOpen(false);
    setSelectedReport(null);
  };

  const getDatePart = (value?: string) => {
    if (!value) return '-';
    return value.split('T')[0] || '-';
  };

  const getTimePart = (value?: string) => {
    if (!value) return '-';
    return value.split('T')[1]?.slice(0, 5) || '-';
  };

  const getEmailName = (email?: string) => {
    if (!email) return '';
    return email.split('@')[0] || '';
  };

  useEffect(() => {
    fetchReports();
  }, []);

  useEffect(() => {
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        closeDetails();
      }
    };

    if (isDetailsOpen) {
      window.addEventListener('keydown', handleEsc);
      document.body.style.overflow = 'hidden';
    }

    return () => {
      window.removeEventListener('keydown', handleEsc);
      document.body.style.overflow = '';
    };
  }, [isDetailsOpen]);

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
          type="button"
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
              <th>Status</th>
              <th>Krótki opis</th>
              <th>Zgłaszający</th>
              <th className="text-center">Akcje</th>
            </tr>
          </thead>

          <tbody>
            {isLoading ? (
              <tr>
                <td colSpan={6} className="table-message">
                  <RefreshCw className="icon-spin text-blue" size={28} />
                  <span>Ładowanie zgłoszeń z bazy danych...</span>
                </td>
              </tr>
            ) : reports.length === 0 && !error ? (
              <tr>
                <td colSpan={6} className="table-message">
                  Brak zgłoszeń w bazie danych.
                </td>
              </tr>
            ) : (
              reports.map((report) => (
                <tr key={report.id} className="table-row">
                  <td className="font-bold text-blue">#{report.id}</td>
                  <td className="font-medium">{report.title}</td>

                  <td>
                    <span className={`status-pill ${report.status ? 'status-active' : 'status-pending'}`}>
                      {report.status || 'Oczekujące'}
                    </span>
                  </td>

                  <td className="truncate-text" title={report.description}>
                    {report.description}
                  </td>

                  <td>
                    <div className="user-info">
                      <span className="user-name">
                        {report.user?.firstName || ''} {report.user?.lastName || ''}
                      </span>

                      {report.user?.email && (
                        <span className="user-badge">
                          {getEmailName(report.user.email)}
                        </span>
                      )}
                    </div>
                  </td>

                  <td>
                    <div className="action-buttons">
                      <button
                        className="btn-action view"
                        title="Zobacz szczegóły"
                        onClick={() => openDetails(report)}
                        type="button"
                      >
                        <Eye size={20} />
                      </button>

                      <button
                        className="btn-action delete"
                        title="Usuń"
                        onClick={() => deleteReport(report.id)}
                        type="button"
                      >
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

      {isDetailsOpen && selectedReport && (
        <div className="report-modal-overlay" onClick={closeDetails}>
          <div
            className="report-modal"
            onClick={(e) => e.stopPropagation()}
            role="dialog"
            aria-modal="true"
            aria-labelledby="report-modal-title"
          >
            <div className="report-modal-header">
              <div>
                <h2 id="report-modal-title">Szczegóły zgłoszenia</h2>
                <p>Pełne informacje o wybranym zgłoszeniu</p>
              </div>

              <button
                className="report-modal-close"
                onClick={closeDetails}
                aria-label="Zamknij okno"
                type="button"
              >
                <X size={22} />
              </button>
            </div>

            <div className="report-modal-body">
              <div className="report-detail-grid">
                <div className="report-detail-card">
                  <span className="report-detail-label">ID zgłoszenia</span>
                  <strong>#{selectedReport.id}</strong>
                </div>

                <div className="report-detail-card">
                  <span className="report-detail-label">Status</span>
                  <strong>{selectedReport.status || 'Oczekujące'}</strong>
                </div>

                <div className="report-detail-card">
                  <span className="report-detail-label">
                    <ClipboardList size={16} />
                    Kategoria
                  </span>
                  <strong>{selectedReport.category?.name || '-'}</strong>
                </div>

                <div className="report-detail-card">
                  <span className="report-detail-label">
                    <ShieldAlert size={16} />
                    Anonimowe
                  </span>
                  <strong>{selectedReport.userAnonymous ? 'Tak' : 'Nie'}</strong>
                </div>

                <div className="report-detail-card report-detail-card-wide">
                  <span className="report-detail-label">Tytuł</span>
                  <strong>{selectedReport.title}</strong>
                </div>

                <div className="report-detail-card">
                  <span className="report-detail-label">
                    <Clock size={16} />
                    Data zdarzenia
                  </span>
                  <strong>{getDatePart(selectedReport.accident_date)}</strong>
                </div>

                <div className="report-detail-card">
                  <span className="report-detail-label">
                    <Clock size={16} />
                    Godzina zdarzenia
                  </span>
                  <strong>{getTimePart(selectedReport.accident_date)}</strong>
                </div>

                <div className="report-detail-card report-detail-card-wide">
                  <span className="report-detail-label">
                    <MapPin size={16} />
                    Miejsce zdarzenia
                  </span>
                  <strong>{selectedReport.location || '-'}</strong>
                </div>

                <div className="report-detail-card report-detail-card-wide">
                  <span className="report-detail-label">
                    <FileText size={16} />
                    Opis zgłoszenia
                  </span>
                  <p>{selectedReport.description}</p>
                </div>

                <div className="report-detail-card report-detail-card-wide">
                  <span className="report-detail-label">
                    <User size={16} />
                    Zgłaszający
                  </span>

                  <div className="report-user-box">
                    <strong>
                      {selectedReport.user?.firstName || ''} {selectedReport.user?.lastName || ''}
                    </strong>

                    {selectedReport.user?.email && (
                      <span>
                        <Mail size={15} />
                        {getEmailName(selectedReport.user.email)}
                      </span>
                    )}
                  </div>
                </div>
              </div>

              <div className="report-modal-footer">
                <button
                  className="action-card secondary report-modal-btn"
                  onClick={closeDetails}
                  type="button"
                >
                  Zamknij
                </button>

                <button
                  className="action-card primary report-modal-btn"
                  onClick={() => deleteReport(selectedReport.id)}
                  type="button"
                >
                  Usuń zgłoszenie
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MojeZgloszenia;