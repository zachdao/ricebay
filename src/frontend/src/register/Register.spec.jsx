import mockAxios from "jest-mock-axios";
import {render, screen} from "@testing-library/react";
import {defaultTheme, Provider} from "@adobe/react-spectrum";
import {MemoryRouter} from "react-router-dom";
import React from "react";
import {Register} from "./Register";

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <Register />
            </MemoryRouter>
        </Provider>,
    );
};

describe('Register', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render register form', async () => {
        await renderComponent();

        expect(screen.queryByLabelText(/First Name.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Last Name.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Alias.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Email.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText(/^Password.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Confirm Password.*/)).toBeInTheDocument();
        expect(screen.queryByLabelText('I Agree to the Above')).toBeInTheDocument();
        const registers = screen.queryAllByText('Register')
        expect(registers.length).toEqual(2);
    });

})