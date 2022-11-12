import { useCallback, useEffect, useState } from 'react';
import axios from 'axios';

export const useHttpQuery = (url, options) => {
    const [appResponse, setAppResponse] = useState(null);
    const [error, setError] = useState(null);
    const [status, setStatus] = useState(null);

    const refetch = useCallback(() => {
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
        // fetch is the native browser API alternative to Axios. Here is how
        // you would accomplish the same as above.
        // The main difference between fetch and Axios, is that fetch will
        // successfully resolve the promise regardless of the status. Axios
        // will throw an error for non-200 status codes.
        //
        // fetch(url, ...options).then(res => {
        //     if (res.status === 200) {
        //         setData(res.data);
        //         setStatus(res.status);
        //         setError(null);
        //     } else {
        //         setData(null);
        //         setError(err);
        //         if (err.response) {
        //             setStatus(err.response.status);
        //         } else {
        //             setStatus(500);
        //         }
        //     }
        // });
    }, []);

    useEffect(refetch, []);

    return { appResponse, error, status, refetch };
};
