import React, { useCallback } from 'react';
import axios from 'axios';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const usePostWithToast = (
    url,
    body,
    deps,
    positive,
    negative,
    onSuccess,
    onFailure,
) => {
    return useCallback(() => {
        axios
            .post(url, body)
            .then(() => {
                toast.custom((t) => (
                    <Toast
                        type="positive"
                        message={positive?.message || 'Updated!'}
                        actionTitle={positive?.actionTitle}
                        actionFn={() => positive?.actionFn(t)}
                        dismissFn={() => toast.remove(t.id)}
                    />
                ));
                if (onSuccess !== null) {
                    onSuccess(body.data);
                }
            })
            .catch((axiosError) => {
                toast.custom((t) => (
                    <Toast
                        type="negative"
                        message={negative?.message || 'Failed to update!'}
                        actionTitle={negative?.actionTitle}
                        actionFn={() => negative?.actionFn(t)}
                        dismissFn={() => toast.remove(t.id)}
                    />
                ));
                if (onFailure !== null) {
                    onFailure(axiosError);
                }
            });
    }, deps);
};