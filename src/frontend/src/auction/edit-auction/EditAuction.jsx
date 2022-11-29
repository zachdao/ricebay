import React, { useCallback, useContext, useState } from 'react';
import {
    Flex,
    Grid,
    Text,
    Button,
    TextField,
    ComboBox,
    Item,
    TextArea,
    RangeCalendar,
    LabeledValue,
    NumberField,
} from '@adobe/react-spectrum';
import PublishCheck from '@spectrum-icons/workflow/PublishCheck';
import Image from '@spectrum-icons/workflow/Image';
import { CategoryTagGroup } from '../../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import {
    getLocalTimeZone,
    today,
    parseDateTime,
    parseDate,
    CalendarDate,
} from '@internationalized/date';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import Alert from '@spectrum-icons/workflow/Alert';
import { useNavigate } from 'react-router-dom';
import { CategoriesContext } from '../../categories.context';

export const EditAuction = ({ auction }) => {
    // Set up text field data
    const [image, setImage] = useState(auction?.images?.[0] || '');
    const [title, setTitle] = useState(auction?.title || '');
    const [startingBid, setStartingBid] = useState(auction?.minimumBid || 1);
    const [bidIncrement, setBidIncrement] = useState(
        auction?.bidIncrement || undefined,
    );
    const [description, setDescription] = useState(auction?.description || '');
    const startDate = auction?.startDate && new Date(auction.startDate);
    const endDate = auction?.endDate && new Date(auction.endDate);
    let [range, setRange] = React.useState({
        start: auction?.startDate
            ? new CalendarDate(
                  startDate.getFullYear(),
                  startDate.getMonth(),
                  startDate.getDate(),
              )
            : today(getLocalTimeZone()),
        end: auction?.endDate
            ? new CalendarDate(
                  endDate.getFullYear(),
                  endDate.getMonth(),
                  endDate.getDate(),
              )
            : today(getLocalTimeZone()).add({ weeks: 1 }),
    });
    const [categories, setCategories] = useState(
        new Set(auction?.categories || []),
    );
    const [error, setError] = useState({});
    const navigate = useNavigate();

    const categoryOptions = useContext(CategoriesContext);

    const undo = usePostWithToast(
        `/auctions/${auction?.id}`,
        auction,
        [auction],
        { message: 'Auction restored!' },
        { message: 'Failed to undo auction save!' },
    );

    const saveAuction = usePostWithToast(
        auction ? `/auctions/${auction.id}` : '/auctions',
        {
            ...auction,
            title,
            image,
            minimumBid: startingBid,
            bidIncrement,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
            categoryIds: categoryOptions
                .filter((cat) => categories.has(cat.name))
                .map((cat) => cat.id),
            description,
        },
        [
            title,
            image,
            startingBid,
            bidIncrement,
            range,
            categories,
            description,
        ],
        {
            message: auction ? 'Auction updated!' : 'Auction created!',
            actionTitle: auction && 'UNDO',
            actionFn: auction && undo,
        },
        { message: 'Failed to save auction!' },
        (appResponse) =>
            !auction && navigate(`/auction/${appResponse.data}/edit`),
        (axiosError) => setError(axiosError?.response?.data),
    );

    const isValid = useCallback(() => {
        const validTitle = title.length > 0;
        const validCategory = categories.size > 0;
        const validStartingBid = startingBid > 0;
        const validBidIncrement =
            bidIncrement !== undefined ? bidIncrement > 0 : true;
        const validDescription = description.length > 0;
        return (
            validTitle &&
            validCategory &&
            validStartingBid &&
            validBidIncrement &&
            validDescription
        );
    }, [title, categories, startingBid, bidIncrement, description]);

    return (
        <Grid
            height="calc(100% - 40px)"
            maxWidth="960px"
            areas={['publish save', 'image details', 'description description']}
            columns={['3fr', '2fr']}
            rows={['auto', 'auto', '1fr']}
            columnGap="size-200"
            rowGap="size-150"
        >
            {/* TODO: Figure out publish status ? */}
            <Button
                variant="primary"
                gridArea="publish"
                maxWidth="120px"
                isDisabled={!isValid()}
            >
                <PublishCheck />
                <Text>Publish</Text>
            </Button>
            <Flex
                direction="row"
                space="size-100"
                justifyContent="right"
                gridArea="save"
            >
                <Button variant="negative" onPress={() => navigate(-1)}>
                    <Text>Cancel</Text>
                </Button>
                <Button
                    variant="primary"
                    marginStart="size-100"
                    onPress={() => saveAuction()}
                    isDisabled={!isValid()}
                >
                    <Text>Save</Text>
                </Button>
            </Flex>
            <ImageUploadWrapper onClick={() => console.log('click')}>
                <Image size="XXL" />
                <Text>Upload Image</Text>
            </ImageUploadWrapper>
            <Grid
                gridArea="details"
                columns={['2fr', '2fr']}
                autoRows="min-content"
                gap="size-100"
                height="100%"
            >
                <TextField
                    label="Auction Title"
                    value={title}
                    onChange={setTitle}
                    width="100%"
                    gridColumnStart="1"
                    gridColumnEnd="3"
                />
                <ComboBox
                    label="Add Category"
                    items={categoryOptions.map((opt) => ({ name: opt }))}
                    onSelectionChange={(selected) =>
                        setCategories((prev) => new Set([...prev, selected]))
                    }
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ComboBox>
                <Flex
                    direction="column"
                    alignItems="start"
                    gap="size-100"
                    height="min-content"
                    width="100%"
                >
                    <LabeledValue label="Categories" />
                    {categories.size ? (
                        <CategoryTagGroup
                            categories={categories}
                            onRemove={(category) =>
                                setCategories(
                                    (prev) =>
                                        new Set(
                                            Array.from(prev).filter(
                                                (el) => el !== category,
                                            ),
                                        ),
                                )
                            }
                        />
                    ) : (
                        <Warning>
                            <Alert color="negative" />
                            <Text>Missing Categories</Text>
                        </Warning>
                    )}
                </Flex>
                <Flex
                    direction="column"
                    gridColumnStart="1"
                    gridColumnEnd="3"
                    justifyContent="start"
                    alignItems="start"
                    height="min-content"
                >
                    <LabeledValue
                        label={`Auction Dates: ${range.start.toString()} to ${range.end.toString()}`}
                    />
                    <RangeCalendar
                        alignSelf="center"
                        aria-label="auction dates"
                        value={range}
                        onChange={setRange}
                    />
                </Flex>

                <NumberField
                    label="Starting Bid"
                    value={startingBid}
                    onChange={setStartingBid}
                    isRequired
                    hideStepper
                    minValue={0}
                    validationState={error?.minimumBid ? 'invalid' : undefined}
                    errorMessage={error?.minimumBid}
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                />
                <NumberField
                    minValue={0}
                    label="Bid Increment"
                    value={bidIncrement}
                    onChange={setBidIncrement}
                    hideStepper
                    validationState={
                        error?.bidIncrement ? 'invalid' : undefined
                    }
                    errorMessage={error?.bidIncrement}
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                />
            </Grid>
            <TextArea
                label="Auction Description"
                gridArea="description"
                width="100%"
                value={description}
                onChange={setDescription}
            />
        </Grid>
    );
};
const ImageUploadWrapper = styled.div`
    grid-area: image;
    width: 100%;
    height: 100%;
    border-width: 2px;
    border-color: grey;
    border-radius: 5px;
    border-style: solid;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
`;

const Warning = styled.div`
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: space-around;
    font-size: 90%;
    font-weight: bold;
    color: var(
        --spectrum-button-warning-text-color,
        var(--spectrum-semantic-negative-color-text-small)
    );
`;
