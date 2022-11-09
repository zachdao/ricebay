import { useEffect, useState } from 'react';
import axios from 'axios';

export const useHttpQuery = (url, options) => {
    const [appResponse, setAppResponse] = useState(null);
    const [error, setError] = useState(null);
    const [status, setStatus] = useState(null);

    useEffect(() => {
        axios
            .request({
                url,
                ...options,
            })
            .then((res) => {
                setAppResponse(res.data);
                setStatus(res.status);
                setError(null);
            })
            .catch((err) => {
                setAppResponse(err?.response?.data);
                setError(err);
                if (err.response) {
                    setStatus(err.response.status);
                } else {
                    setStatus(500);
                }
            });
    }, []);

    return { appResponse, error, status };
};
