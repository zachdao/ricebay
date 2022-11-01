import { useEffect, useState } from 'react';
import axios from 'axios';

export const useHttpQuery = (url, options) => {
    const [data, setData] = useState();
    const [error, setError] = useState();
    const [status, setStatus] = useState();

    useEffect(() => {
        axios
            .request({
                url,
                ...options,
            })
            .then((res) => {
                setData(res.data);
                setStatus(res.status);
                setError(null);
            })
            .catch((err) => {
                setData(null);
                setError(err);
                if (err.response) {
                    setStatus(err.response.status);
                } else {
                    setStatus(500);
                }
            });
    });

    return { data, error, status };
};
