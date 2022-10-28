import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { AppName } from './AppName';

describe('AppName', () => {
    it('should be "RiceBay" if the browser theme is light', async () => {
        await render(<AppName />);

        expect(window.matchMedia).toHaveBeenCalledWith(
            '(prefers-color-scheme: dark)',
        );
        expect(screen.queryByText('RiceBay')).toBeInTheDocument();
    });

    it('should be "RiceRoad" if the browser theme is dark', async () => {
        Object.defineProperty(window, 'matchMedia', {
            writable: true,
            value: jest.fn().mockImplementation((query) => ({
                matches: true, // THIS IS WHAT MAKES IT DARK THEME
                media: query,
                onchange: null,
                addListener: jest.fn(), // Deprecated
                removeListener: jest.fn(), // Deprecated
                addEventListener: jest.fn(),
                removeEventListener: jest.fn(),
                dispatchEvent: jest.fn(),
            })),
        });

        await render(<AppName />);

        expect(window.matchMedia).toHaveBeenCalledWith(
            '(prefers-color-scheme: dark)',
        );
        await waitFor(() => {
            expect(screen.queryByText('RiceRoad')).toBeInTheDocument();
        });
    });
});
