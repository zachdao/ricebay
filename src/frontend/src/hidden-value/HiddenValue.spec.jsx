import * as React from 'react';
import { render, screen } from '@testing-library/react';
import { HiddenValue } from './HiddenValue';
import userEvent from '@testing-library/user-event';

describe('HiddenValue', () => {
    const testValue = 'test';
    it('should hide the value by default', async () => {
        await render(<HiddenValue value={testValue} />);

        expect(screen.queryByText(testValue)).toBeNull();
        expect(screen.getByRole('button')).toBeInTheDocument();
    });

    it('should display the text when the show button is clicked', async () => {
        const user = userEvent.setup();
        await render(<HiddenValue value={testValue} />);

        await user.click(screen.getByRole('button'));

        expect(screen.queryByText(testValue)).toBeInTheDocument();
    });
});
