import { useState, useCallback, useMemo } from 'react';
import type { ChangeEvent, FormEvent } from 'react';

export default function Shortener() {
  const [url, setUrl] = useState<string>('');
  const [customAlias, setCustomAlias] = useState<string>('');
  const [shortUrl, setShortUrl] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');

  const isValidUrl = useMemo(() => {
    if (!url) return false;
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }, [url]);

  const handleSubmit = useCallback(async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!isValidUrl) {
      setError('Please enter a valid URL');
      return;
    }

    setLoading(true);
    setError('');
    setShortUrl('');

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 10000); // 10s timeout

      const payload: { url: string; customAlias?: string } = { url };
      if (customAlias.trim()) {
        payload.customAlias = customAlias.trim();
      }

      const response = await fetch('/api/shorten', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Cache-Control': 'no-cache'
        },
        body: JSON.stringify(payload),
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Shortening failed');
      }

      const result = await response.text();
      const transformed = result.replace('http://localhost:8080', window.location.origin.replace('5173', '8080'));
      setShortUrl(transformed);
      
      // Reset form on success
      setUrl('');
      setCustomAlias('');
  
    } catch (err) {
      if (err instanceof Error) {
        setError(err.name === 'AbortError' ? 'Request timed out' : err.message);
      } else {
        setError('Failed to shorten URL');
      }
    } finally {
      setLoading(false);
    }
  }, [url, customAlias, isValidUrl]);

  const handleCopyToClipboard = useCallback(async () => {
    try {
      await navigator.clipboard.writeText(shortUrl);
    } catch (err) {
      console.error('Failed to copy to clipboard:', err);
    }
  }, [shortUrl]);

  return (
    <div style={{ maxWidth: 500, margin: 'auto', padding: '2rem' }}>
      <h2>🔗 URL Shortener</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="url"
          value={url}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setUrl(e.target.value)}
          placeholder="Enter a long URL (e.g., https://example.com)"
          style={{ 
            width: '100%', 
            padding: '0.75rem', 
            fontSize: '1rem',
            marginBottom: '0.5rem',
            border: `2px solid ${!isValidUrl && url ? '#ff6b6b' : '#ddd'}`,
            borderRadius: '4px'
          }}
          required
        />
        <input
          type="text"
          value={customAlias}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setCustomAlias(e.target.value)}
          placeholder="Custom alias (optional, 3-12 chars)"
          style={{ 
            width: '100%', 
            padding: '0.75rem', 
            fontSize: '1rem',
            marginBottom: '1rem',
            border: '2px solid #ddd',
            borderRadius: '4px'
          }}
          maxLength={12}
          pattern="[a-zA-Z0-9_-]*"
        />
        <button 
          type="submit" 
          disabled={loading || !isValidUrl}
          style={{ 
            padding: '0.75rem 1.5rem',
            fontSize: '1rem',
            backgroundColor: loading || !isValidUrl ? '#ccc' : '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: loading || !isValidUrl ? 'not-allowed' : 'pointer',
            transition: 'background-color 0.2s'
          }}
        >
          {loading ? 'Shortening...' : 'Shorten URL'}
        </button>
      </form>

      {shortUrl && (
        <div style={{ 
          marginTop: '1.5rem', 
          padding: '1rem',
          backgroundColor: '#f8f9fa',
          borderRadius: '4px',
          border: '1px solid #dee2e6'
        }}>
          <strong>Short URL:</strong>{' '}
          <a 
            href={shortUrl} 
            target="_blank" 
            rel="noopener noreferrer"
            style={{ 
              color: '#007bff',
              textDecoration: 'none',
              wordBreak: 'break-all'
            }}
          >
            {shortUrl}
          </a>
          <button
            onClick={handleCopyToClipboard}
            style={{
              marginLeft: '0.5rem',
              padding: '0.25rem 0.5rem',
              fontSize: '0.8rem',
              backgroundColor: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '3px',
              cursor: 'pointer'
            }}
          >
            Copy
          </button>
        </div>
      )}

      {error && (
        <div style={{ 
          marginTop: '1rem',
          padding: '0.75rem',
          backgroundColor: '#f8d7da',
          color: '#721c24',
          border: '1px solid #f5c6cb',
          borderRadius: '4px'
        }}>
          {error}
        </div>
      )}
    </div>
  );
}