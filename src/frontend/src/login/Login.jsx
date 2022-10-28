import React, { useCallback, useEffect, useState } from 'react';
import {
    Button,
    ButtonGroup,
    Heading,
    Provider,
    TextField,
} from '@adobe/react-spectrum';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Toast } from '../toast/Toast';
import toast from 'react-hot-toast';
import styled from 'styled-components';
import { AppName } from '../app-name/AppName';

export const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const login = useCallback(async () => {
        try {
            setError('');
            await axios.post('/accounts/login', {
                email,
                password,
            });
            navigate('/');
        } catch (e) {
            setError('Invalid email or password!');
        }
    }, [email, password]);

    useEffect(() => {
        if (error) {
            toast.custom((t) => {
                return (
                    <Toast
                        message={error}
                        type="negative"
                        dismissFn={() => toast.remove(t.id)}
                    />
                );
            });
        }
    }, [error]);

    const isValid = useCallback(() => {
        return email.endsWith('@rice.edu') && password.length > 1;
    }, [email, password]);

    return (
        <Provider colorScheme="dark">
            <LoginWrapper>
                <Heading level="1" marginTop="20%" marginBottom="50px">
                    <AppName />
                </Heading>
                <TextField
                    validationState={error ? 'invalid' : undefined}
                    label="Email"
                    value={email}
                    onChange={setEmail}
                    type="email"
                    onBlur={() => setError('')}
                />
                <TextField
                    validationState={error ? 'invalid' : undefined}
                    label="Password"
                    value={password}
                    onChange={setPassword}
                    type="password"
                    onBlur={() => setError('')}
                />
                <ButtonGroup marginTop="10px">
                    <Button
                        variant="cta"
                        onPress={login}
                        isDisabled={!isValid()}
                    >
                        Login
                    </Button>
                    <Button
                        variant="secondary"
                        onPress={() => navigate('/register')}
                    >
                        Register
                    </Button>
                </ButtonGroup>
            </LoginWrapper>
        </Provider>
    );
};

const LoginWrapper = styled.div`
    display: flex;
    flex-direction: column;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: start;
    background-image: url('/images/login.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
`;
