import React from 'react';
import { Sidebar } from './Sidebar';
import { render, screen } from '@testing-library/react';
import {MemoryRouter} from "react-router-dom";

const renderComponent = () => {
    render(
        <MemoryRouter>
            <Sidebar />
        </MemoryRouter>
    )
}

describe('Sidebar', () => {
    it('should render the nav list', () => {
        renderComponent();
        expect(screen.queryByText('Home')).toBeInTheDocument();
    });
});
