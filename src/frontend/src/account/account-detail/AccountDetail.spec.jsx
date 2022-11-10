import React from 'react';
import { render, screen } from '@testing-library/react';
import { AccountDetail } from './AccountDetail';

describe('Account Detail', () => {
    const testAccount = {
        givenName: 'test first name',
        surname: 'test last name',
        alias: 'test alias',
        email: 'test@rice.edu',
        zelleId: 'zelleid1234',
    };
    it('should render an account', async () => {
        await render(<AccountDetail account={testAccount} />);

        expect(
            screen.queryByText(new RegExp(`.*${testAccount.givenName}.*`)),
        ).toBeInTheDocument();
        expect(
            screen.queryByText(new RegExp(`.*${testAccount.surname}.*`)),
        ).toBeInTheDocument();
        expect(
            screen.queryByText(new RegExp(`.*${testAccount.alias}.*`)),
        ).toBeInTheDocument();
        expect(screen.queryByText(testAccount.email)).toBeInTheDocument();
        expect(screen.queryByText(testAccount.zelleId)).toBeNull();
    });
});
