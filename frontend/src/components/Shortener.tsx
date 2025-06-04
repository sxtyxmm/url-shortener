import { useState } from 'react';

export default function Shortener() {
  const [url, setUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setShortUrl('');

    try {
      const response = await fetch('/api/shorten', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ url }),
      });

      if (!response.ok) throw new Error('Shortening failed');

      const result = await response.text();
      // setShortUrl(result); // which returns http://localhost:8080/xyz123
      const transformed = result.replace('http://localhost:8080', window.location.origin.replace('5173', '8080'));
      setShortUrl(transformed);
  
    } catch (err) {
      setError('Failed to shorten URL');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 500, margin: 'auto', padding: '2rem' }}>
      <h2>🔗 URL Shortener</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Enter a long URL"
          style={{ width: '100%', padding: '0.5rem', fontSize: '1rem' }}
        />
        <button type="submit" style={{ marginTop: '1rem', padding: '0.5rem 1rem' }}>
          {loading ? 'Shortening...' : 'Shorten'}
        </button>
      </form>

      {shortUrl && (
        <div style={{ marginTop: '1rem' }}>
          <strong>Short URL:</strong>{' '}
          <a href={shortUrl} target="_blank" rel="noopener noreferrer">
            {shortUrl}
          </a>
        </div>
      )}

      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}